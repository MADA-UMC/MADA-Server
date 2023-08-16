package com.umc.mada.saying.service;

import com.umc.mada.notice.domain.Notice;
import com.umc.mada.notice.dto.NoticeListResponseDto;
import com.umc.mada.saying.domain.Saying;
import com.umc.mada.saying.dto.SayingResponseDto;
import com.umc.mada.saying.repository.SayingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SayingService {
    private final SayingRepository sayingRepository;

    @Transactional
    public List<SayingResponseDto> findAllSaying() {
        try {
            List<Saying> sayingList = sayingRepository.findAllDesc();
            List<SayingResponseDto> responseDtoList = new ArrayList<>();
            for (Saying saying : sayingList) {
                responseDtoList.add(
                        new SayingResponseDto(saying)
                );
            }
            return responseDtoList;
        } catch (Exception e) {
        }
        return null;
    }
}
