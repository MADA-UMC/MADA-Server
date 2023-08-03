package com.umc.mada.notice.repository;

import com.umc.mada.notice.domain.Notice;
import com.umc.mada.notice.dto.NoticeListResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n ORDER BY n.id DESC")
    List<NoticeListResponseDto> findAllDesc();
}
