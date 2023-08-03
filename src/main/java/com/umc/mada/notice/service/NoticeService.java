package com.umc.mada.notice.service;

import com.umc.mada.notice.domain.Notice;
import com.umc.mada.notice.dto.NoticeListResponseDto;
import com.umc.mada.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public List<NoticeListResponseDto> findAllNotice() {
        try {
            List<Notice> noticeList = noticeRepository.findAll();
            List<NoticeListResponseDto> responseDtoList = new ArrayList<>();
            for (Notice notice : noticeList) {
                responseDtoList.add(
                        new NoticeListResponseDto(notice)
                );
            }
            return responseDtoList;
        } catch (Exception e) {
        }
        return null;
    }
}
