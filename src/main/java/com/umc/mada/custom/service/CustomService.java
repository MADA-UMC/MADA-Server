package com.umc.mada.custom.service;

import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.custom.domain.ItemType;
import com.umc.mada.custom.dto.CustomItemsResponse;
import com.umc.mada.custom.dto.ItemElementResponseDto;
import com.umc.mada.custom.dto.UserCharacterResponse;
import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.custom.repository.HaveItemRepository;
import com.umc.mada.exception.BuyOwnedItemException;
import com.umc.mada.exception.DuplicationItemException;
import com.umc.mada.exception.ErrorType;
import com.umc.mada.exception.NotAllowToWearingException;
import com.umc.mada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomService {

    private final CustomRepository customRepository;
    private final HaveItemRepository haveItemRepository;
    private final String ATTENDANCE_MISSION = "mission";
//    private final AmazonS3 amazonS3;
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    public UserCharacterResponse printUserCharacter(User user){
        List<CustomItem> customItems = haveItemRepository.findCustomItemByUserAndWearing(user, true);
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

    public CustomItemsResponse findItemsByType(User user, String itemType) {
        ItemType type;
        try {
            type = ItemType.getTypeCode(itemType);
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 타입는 없는 아이템 타입입니다.");
        }

        //해당 타입의 아이템 목록 가져오기
        List<CustomItem> itemList = customRepository.findCustomItemByItemType(type);

        //출석 아이템을 사용자가 소유하고 있는지 확인하기
        CustomItemsResponse customItemsResponse = new CustomItemsResponse();
        for(CustomItem item : itemList){
            //출석 아이템이 아닌 기본 아이템은 넘어가기
            if(!item.getCategory().equals(ATTENDANCE_MISSION)) continue;

            //출석 아이템을 소유하고 있지 않다면 목록에서 삭제하기
            Optional<HaveItem> haveItemOptional = haveItemRepository.findByCustomItemAndUser(item, user);
            if(!haveItemOptional.isPresent()){
                itemList.remove(item);
            }

            customItemsResponse.addItem(ItemElementResponseDto.of(item));
        }

        return customItemsResponse;
    }

    public void changeUserItem(User user, List<String> items_id){//List<String>  String[] items_id
        boolean colorCheck = false;
        Set<String> itemsCategory = new HashSet<>();
        for(String item_id : items_id){

            Optional<CustomItem> customItem = customRepository.findCustomItemById(Integer.valueOf(item_id));
            CustomItem item = customItem.get();

            //미션 아이템이라면 소유한 아이템인지 확인
            if(item.getCategory().equals(ATTENDANCE_MISSION)) {
                Optional<HaveItem> newHaveItemOptional = haveItemRepository.findByCustomItemAndUser(item, user);
                //소유한 아이템이 아니라면 예외발생
                if (!newHaveItemOptional.isPresent()) {
                    throw new NotAllowToWearingException(ErrorType.NOT_ALLOW_TO_WEARING.getMessage());
                }
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
            if(customItem.get().getItemType().equals(ItemType.I1)){
                colorCheck = true;
            }
        }

        //이전에 입고 있던 아이템 없애기
        List<HaveItem> oldhaveItemList = haveItemRepository.findByUserAndWearing(user, true);
        for(HaveItem oldItem: oldhaveItemList){ //예전에 입은 아이템은 false로 해준다.
            oldItem.updateHaveItemWearing(false);
            haveItemRepository.save(oldItem);
        }

        //새로운 아이템 입기
        for(String item_id : items_id){
            Optional<CustomItem> customItem = customRepository.findCustomItemById(Integer.valueOf(item_id));
            Optional<HaveItem> newHaveItemOptional = haveItemRepository.findByCustomItemAndUser(customItem.get(), user);

            //새로운 아이템 입히기
            HaveItem newHaveItem = newHaveItemOptional.get();
            newHaveItem.updateHaveItemWearing(true);
            haveItemRepository.save(newHaveItem);
        }

        //color를 착용하지 않았을 경우 color 디폴트 설정하기
        if(!colorCheck){
            Optional<HaveItem> defaultItemOptional = haveItemRepository.findByCustomItemAndUser(customRepository.findCustomItemById(10).get(), user);
            HaveItem defaultHaveItem = defaultItemOptional.get();
            defaultHaveItem.updateHaveItemWearing(true);
            haveItemRepository.save(defaultHaveItem);
        }
    }

    public void resetCharcter(User user){
        List<HaveItem> oldsetItemList = haveItemRepository.findByUser(user); //TODO: 예외처리 추가하기(커스텀 예외처리)
        for(int i=0; i<oldsetItemList.size(); i++){
            HaveItem oldsetItem = oldsetItemList.get(i);
            oldsetItem.updateHaveItemWearing(false);
            haveItemRepository.save(oldsetItem);
        }
        //컬러 디폴트 설정하기
        Optional<HaveItem> defaultItemOptional = haveItemRepository.findByCustomItemAndUser(customRepository.findCustomItemById(10).get(), user);
        HaveItem defaultHaveItem = defaultItemOptional.get();
        defaultHaveItem.updateHaveItemWearing(true);
        haveItemRepository.save(defaultHaveItem);
    }

    public void buyItem(User user, int item_id){
        Optional<CustomItem> customItemOptional = customRepository.findCustomItemById(item_id);
        CustomItem customItem = customItemOptional.get();
        //haveItemRepository.findByCustomItemAndUser(customItem, user).orElseThrow(() -> new BuyOwnedItemException(ErrorType.BUY_OWNED_ITEM_ERROR.getMessage()));
        haveItemRepository.findByCustomItemAndUser(customItem, user).ifPresent(item -> {
            throw new BuyOwnedItemException(ErrorType.BUY_OWNED_ITEM_ERROR.getMessage());
        });
//        Optional<HaveItem> ha = haveItemRepository.findByCustomItemAndUser(customItem, user);
//        if(haveItemRepository.findByCustomItemAndUser(customItem, user).isPresent()){
//           //throw new IllegalArgumentException("이미 결제된, 가지고 있는 아이템입니다."); //TODO: 커스텀 예외처리
//            System.out.println("sdjlkfa");
//            throw new BuyOwnedItemException(ErrorType.BUY_OWNED_ITEM_ERROR.getMessage());
//        }
        //TODO: 결제창으로 넘어가서 아이템 결제하는 부분 구현하기
        haveItemRepository.save(new HaveItem(user, customItem));
    }

}
