package com.umc.mada.my.repository;

import com.umc.mada.my.domain.My;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyRepository extends JpaRepository<My, Long> {
    @Query(value = "SELECT * FROM SAYING ORDER BY RAND() limit 1", nativeQuery = true)
    List<My> findAllDesc();
}
