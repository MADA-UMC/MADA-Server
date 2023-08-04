package com.umc.mada.custom.service;

import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.custom.repository.HaveItemRepository;
import com.umc.mada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomService {

    private final CustomRepository customRepository;
    private final HaveItemRepository haveItemRepository;

    public void changeUserItem(User user, Long item_id){
        Optional<HaveItem> newhaveItemOptional = haveItemRepository.findByIdAndUser(item_id, user); //TODO: 예외처리 추가하기
        HaveItem newhaveItem = newhaveItemOptional.get();

//        Optional<HaveItem> oldhaveItemOptional = user.getHaveItems().stream() //TODO: 예외처리 추가하기(커스텀 예외처리)
//                .filter(HaveItem::isWearing)
//                .reduce((a,b) -> {
//                    try {
//                        throw new Exception("캐릭터변경오류발생");
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//        HaveItem oldhaveItem = oldhaveItemOptional.get();
        //TODO: 아이템 타입 보고 같은 타입 아이템 wearing을 false로 해야함
        List<HaveItem> oldhaveItemList = haveItemRepository.findByUserAndWearing(user, true);
        for(int i=0; i<oldhaveItemList.size(); i++){
            HaveItem oldhaveItem = oldhaveItemList.get(i);
            oldhaveItem.updateHaveItemWearing(false);
            haveItemRepository.save(oldhaveItem);
        }

        newhaveItem.updateHaveItemWearing(true);
//        oldhaveItem.updateHaveItemWearing(false);

        haveItemRepository.save(newhaveItem);
//        haveItemRepository.save(oldhaveItem);
    }

    public void resetCharcter(User user){
        List<HaveItem> oldsetItemList = haveItemRepository.findByUser(user); //TODO: 예외처리 추가하기(커스텀 예외처리)
        for(int i=0; i<oldsetItemList.size(); i++){
            HaveItem oldsetItem = oldsetItemList.get(i);
            oldsetItem.updateHaveItemWearing(false);
            haveItemRepository.save(oldsetItem);
        }
    }

}
