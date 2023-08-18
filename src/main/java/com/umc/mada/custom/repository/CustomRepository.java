package com.umc.mada.custom.repository;

import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomRepository extends JpaRepository<CustomItem, Long> {
    Optional<CustomItem> findCustomItemById(int id);

    List<CustomItem> findCustomItemByItemType(ItemType type);
}
