package com.umc.mada.custom.service;

import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.custom.domain.ItemType;
import com.umc.mada.custom.domain.WearingItem;
import com.umc.mada.custom.dto.CustomItemsResponse;
import com.umc.mada.custom.dto.ItemElementResponse;
import com.umc.mada.custom.dto.UserCharacterResponse;
import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.custom.repository.HaveItemRepository;
import com.umc.mada.custom.repository.WearingItemRepository;
import com.umc.mada.exception.BuyOwnedItemException;
import com.umc.mada.exception.DuplicationItemException;
import com.umc.mada.exception.ErrorType;
import com.umc.mada.exception.NotAllowToWearingException;
import com.umc.mada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomService {

    private final CustomRepository customRepository;
    private final HaveItemRepository haveItemRepository;
    private final WearingItemRepository wearingItemRepository;
//    private final AmazonS3 amazonS3;
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    public UserCharacterResponse printUserCharacter(User user){
//        List<CustomItem> customItems = haveItemRepository.findCustomItemByUserAndWearing(user, true);
        List<CustomItem> customItems = wearingItemRepository.findCustomItemByUser(user);
        return UserCharacterResponse.of(customItems);

        // S3 url 부분
//        String url = amazonS3.getUrl(bucket, customItems.get(0).getFileName()).toString();
//        System.out.println(url);

        //사용자 캐릭터가 디폴트 값이라면 디폴트 캐릭터 데이터를 넘겨준다.
//        List<CustomItem> colorItems = customItems.stream().filter(item -> ItemType.I1.equals(item.getItemType())).collect(Collectors.toList());
//        if(customItems.isEmpty()|| colorItems.isEmpty()){
//            //return new UserCharacterResponse(CharacterItemResponse.of(customRepository.findCustomItemById(1L).get()));
//            customItems.add(customRepository.findCustomItemById(10).get()); //TODO: isPresent() 체크하기
//        }
    }

    public CustomItemsResponse getItemList(User user){
        List<CustomItem> itemList = customRepository.findAll();
        //사용자가 해당 아이템들을 소유하고 있는지 확인하기
        return checkHaveItem(itemList, user);
    }

    public CustomItemsResponse findItemsByType(User user, String itemType) {
        ItemType type;
        try {
            type = ItemType.getTypeCode(itemType);
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 타입는 없는 아이템 타입입니다.");
        }

        //해당 타입의 아이템 목록 가져오기
        List<CustomItem> itemList = customRepository.findCustomItemByItemType(type);

        //사용자가 해당 아이템들을 소유하고 있는지 확인하기
        return checkHaveItem(itemList, user);
    }

    public CustomItemsResponse checkHaveItem(List<CustomItem> itemList, User user){ //사용자가 아이템을 소유하고 있는지 확인하는 함수
        CustomItemsResponse customItemsResponse = CustomItemsResponse.builder().build();
        for(CustomItem item : itemList){
            //아이템을 소유하고 있는지 확인
            boolean have = false;
            boolean haveItemCheck = haveItemRepository.existsByCustomItemAndUser(item, user);

            //출석 아이템인데 소유하고 있지 않다면 목록에 추가하지 않기
            if(!haveItemCheck && item.getUnlockCondition().equals(CustomItem.ItemUnlockCondition.ATTENDANCE)) continue;

            //해당 아이템을 소유하고 있다면 true;
            if(haveItemCheck) have = true;

            customItemsResponse.addItem(ItemElementResponse.of(item, have));
        }
        return customItemsResponse;
    }

    @Transactional
    public UserCharacterResponse changeUserItem(User user, List<Integer> items_id){//List<String>  String[] items_id
        List<CustomItem> customItems  = new ArrayList<>();
        boolean colorCheck = false;
        Set<String> itemsCategory = new HashSet<>();

        //착용 아이템을 바꾸기 전에 착용 조건 체크
        for(Integer item_id : items_id){
            CustomItem item = customRepository.findCustomItemById(item_id).orElseThrow(()-> new RuntimeException("없는 아이템 ID입니다."));

            //소유한 아이템인지 확인
            boolean haveCheck = haveItemRepository.existsByCustomItemAndUser(item, user);
            if(!haveCheck){
                throw new NotAllowToWearingException(ErrorType.NOT_ALLOW_TO_WEARING.getMessage());
            }

            //아이템의 카테고리가 겹치지 않도록 확인
            String[] itemCategories = item.getCategory().split(",");
            for(String category: itemCategories){
                if(itemsCategory.contains(category)){
                    throw new DuplicationItemException(ErrorType.DUPLICATE_ITEM_CATEGORY.getMessage());
                }
                itemsCategory.add(category);
            }

            //컬러를 지정했는지 확인
            if(item.getItemType().equals(ItemType.I1)){
                colorCheck = true;
            }
        }

        //이전에 입고 있던 아이템 없애기
        List<WearingItem> oldWearingItemList = wearingItemRepository.findByUser(user);
        wearingItemRepository.deleteAll(oldWearingItemList); //TODO: 소프트 삭제로 바꾸기

        //새로운 아이템 입기
        for(Integer item_id : items_id){
            CustomItem newItem = customRepository.findCustomItemById(item_id).orElseThrow(()-> new RuntimeException("없는 아이템 ID입니다."));
            wearingItemRepository.save(WearingItem.builder().user(user).customItem(newItem).build());
            customItems.add(newItem);
        }

        //color를 착용하지 않았을 경우 color 디폴트 설정하기
        if(!colorCheck){
            wearingItemRepository.save(WearingItem.builder().user(user).customItem(customRepository.findById(10L).orElseThrow(()-> new RuntimeException("기본 색상 ID가 없습니다."))).build());
        }
        return UserCharacterResponse.of(customItems);
    }

    public UserCharacterResponse resetCharcter(User user){
        List<CustomItem> customItems  = new ArrayList<>();
        List<Integer> initialItems = new ArrayList<>(Arrays.asList(10,48,49,50)); //디폴트 값(초기값)
        //이전 캐릭터 아이템 지우기
        List<WearingItem> oldSetItemList = wearingItemRepository.findByUser(user);
        wearingItemRepository.deleteAll(oldSetItemList);
        //초기 값으로 설정하기
        for(Integer itemId : initialItems){
            CustomItem item = customRepository.findCustomItemById(itemId).orElseThrow(()-> new RuntimeException("없는 아이템입니다.")); //TODO: 예외처리 바꿔야 함
            wearingItemRepository.save(WearingItem.builder().user(user).customItem(item).build());
            customItems.add(item);
        }
        return UserCharacterResponse.of(customItems);
    }

    public void buyItem(User user, int item_id){ //TODO: 코드 리팩토링 필요
        Optional<CustomItem> customItemOptional = customRepository.findCustomItemById(item_id);
        CustomItem customItem = customItemOptional.get();
        haveItemRepository.findByCustomItemAndUser(customItem, user).orElseThrow(() -> new BuyOwnedItemException(ErrorType.BUY_OWNED_ITEM_ERROR.getMessage()));

//        Optional<HaveItem> ha = haveItemRepository.findByCustomItemAndUser(customItem, user);
//        if(haveItemRepository.findByCustomItemAndUser(customItem, user).isPresent()){
//           //throw new IllegalArgumentException("이미 결제된, 가지고 있는 아이템입니다."); //TODO: 커스텀 예외처리
//            System.out.println("sdjlkfa");
//            throw new BuyOwnedItemException(ErrorType.BUY_OWNED_ITEM_ERROR.getMessage());
//        }
        //TODO: 결제창으로 넘어가서 아이템 결제하는 부분 구현하기
        haveItemRepository.save(HaveItem.builder().user(user).customItem(customItem).build());
    }

}
