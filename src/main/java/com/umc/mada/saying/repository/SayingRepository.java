package com.umc.mada.saying.repository;

import com.umc.mada.saying.domain.Saying;
import com.umc.mada.saying.dto.SayingResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SayingRepository extends JpaRepository<Saying, Long> {
    @Query(value = "SELECT * FROM SAYING ORDER BY RAND() limit 1", nativeQuery = true)
    List<Saying> findAllDesc();
}
