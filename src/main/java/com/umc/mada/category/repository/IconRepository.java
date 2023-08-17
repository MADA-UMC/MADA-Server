package com.umc.mada.category.repository;

import com.umc.mada.category.domain.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IconRepository extends JpaRepository<Icon, Integer> {
    Optional<Icon> findIconById(Integer id);
}
