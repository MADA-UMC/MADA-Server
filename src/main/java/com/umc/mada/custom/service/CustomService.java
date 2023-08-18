package com.umc.mada.custom.service;

import com.amazonaws.services.s3.AmazonS3;
import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.custom.domain.ItemType;
import com.umc.mada.custom.dto.CustomItemsResponse;
import com.umc.mada.custom.dto.ItemsElementResponse;
import com.umc.mada.custom.dto.UserCharacterResponse;
import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.custom.repository.HaveItemRepository;
import com.umc.mada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomService {

    private final CustomRepository customRepository;
    private final HaveItemRepository haveItemRepository;
//    private final AmazonS3 amazonS3;
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    public UserCharacterResponse printUserCharacter(User user){
        //List<HaveItem> wearingItems = haveItemRepository.findByUserAndWearing(user, true); //사용자가 보유한 아이템 중 착용하고 있는 아이템
        List<CustomItem> customItems = haveItemRepository.findCustomItemByUserAndWearing(user, true);
        // S3 url 부분
//        String url = amazonS3.getUrl(bucket, customItems.get(0).getFileName()).toString();
//        System.out.println(url);

        List<CustomItem> colorItems = customItems.stream().filter(item -> ItemType.I1.equals(item.getItemType())).collect(Collectors.toList());
        if(customItems.isEmpty()|| colorItems.isEmpty()){ //사용자 캐릭터가 디폴트 값이라면 디폴트 캐릭터 데이터를 넘겨준다.
            //return new UserCharacterResponse(CharacterItemResponse.of(customRepository.findCustomItemById(1L).get()));
            customItems.add(customRepository.findCustomItemById(1).get()); //TODO: isPresent() 체크하기
        }
        return UserCharacterResponse.of(customItems);
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

        //아이템을 사용자가 소유하고 있는지 확인하기
        CustomItemsResponse customItemsResponse = new CustomItemsResponse();
        for(CustomItem item : itemList){
            boolean have = false;
            Optional<HaveItem> haveItemOptional = haveItemRepository.findByCustomItemAndUser(item, user);
            if(haveItemOptional.isPresent()){
                have = true;
            }
            ItemsElementResponse itemsElementResponse = ItemsElementResponse.of(item, have);
            customItemsResponse.addItem(itemsElementResponse);
        }

        return customItemsResponse;
    }

//    public void changeUserItem(User user, int[] items_id){
//        for(int item_id : items_id){
//            Optional<CustomItem> customItem = customRepository.findCustomItemById(item_id);
//            Optional<HaveItem> newhaveItemOptional = haveItemRepository.findByCustomItemAndUser(customItem.get(), user); //TODO: 예외처리 추가하기
//            HaveItem newhaveItem = newhaveItemOptional.get();
//            List<HaveItem> oldhaveItemList = haveItemRepository.findByUserAndWearing(user, true);
//            for(int i=0; i<oldhaveItemList.size(); i++){ //'for' loop can be replaced with enhanced 'for'
//                HaveItem oldhaveItem = oldhaveItemList.get(i);
//                //입으려는 아이템 타입과 같은 아이템 타입만 false로 해준다.
//                if(newhaveItem.getCustomItem().getItemType().equals(oldhaveItem.getCustomItem().getItemType())){
//                    oldhaveItem.updateHaveItemWearing(false);
//                    haveItemRepository.save(oldhaveItem);
//                }
//            }
//            newhaveItem.updateHaveItemWearing(true);
//            haveItemRepository.save(newhaveItem);
//        }
////        Optional<CustomItem> customItem = customRepository.findCustomItemById(item_id);
////        Optional<HaveItem> newhaveItemOptional = haveItemRepository.findByCustomItemAndUser(customItem.get(), user); //TODO: 예외처리 추가하기
////        HaveItem newhaveItem = newhaveItemOptional.get();
////
//////        Optional<HaveItem> oldhaveItemOptional = user.getHaveItems().stream() //TODO: 예외처리 추가하기(커스텀 예외처리)
//////                .filter(HaveItem::isWearing)
//////                .reduce((a,b) -> {
//////                    try {
//////                        throw new Exception("캐릭터변경오류발생");
//////                    } catch (Exception e) {
//////                        throw new RuntimeException(e);
//////                    }
//////                });
//////        HaveItem oldhaveItem = oldhaveItemOptional.get();
////        //TODO: 아이템 타입 보고 같은 타입 아이템 wearing을 false로 해야함
////        List<HaveItem> oldhaveItemList = haveItemRepository.findByUserAndWearing(user, true);
////        for(int i=0; i<oldhaveItemList.size(); i++){ //'for' loop can be replaced with enhanced 'for'
////            HaveItem oldhaveItem = oldhaveItemList.get(i);
////            //입으려는 아이템 타입과 같은 아이템 타입만 false로 해준다.
////            if(newhaveItem.getCustomItem().getItemType().equals(oldhaveItem.getCustomItem().getItemType())){
////                oldhaveItem.updateHaveItemWearing(false);
////                haveItemRepository.save(oldhaveItem);
////            }
////        }
////
////        newhaveItem.updateHaveItemWearing(true);
//////        oldhaveItem.updateHaveItemWearing(false);
////
////        haveItemRepository.save(newhaveItem);
////        haveItemRepository.save(oldhaveItem);
//    }

    public void resetCharcter(User user){
        List<HaveItem> oldsetItemList = haveItemRepository.findByUser(user); //TODO: 예외처리 추가하기(커스텀 예외처리)
        for(int i=0; i<oldsetItemList.size(); i++){
            HaveItem oldsetItem = oldsetItemList.get(i);
            oldsetItem.updateHaveItemWearing(false);
            haveItemRepository.save(oldsetItem);
        }
    }

    public void buyItem(User user, int item_id){
        Optional<CustomItem> customItemOptional = customRepository.findCustomItemById(item_id);
        CustomItem customItem = customItemOptional.get();
        if(haveItemRepository.findByCustomItemAndUser(customItem, user).isPresent()){
           throw new IllegalArgumentException("이미 결제된, 가지고 있는 아이템입니다."); //TODO: 커스텀 예외처리
        }
        //TODO: 결제창으로 넘어가서 아이템 결제하는 부분 구현하기
        haveItemRepository.save(new HaveItem(user, customItem));
    }

}
