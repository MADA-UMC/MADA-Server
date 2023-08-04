package com.umc.mada.custom.repository;

import com.umc.mada.custom.domain.CustomItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepository extends JpaRepository<CustomItem, Long> {

}
