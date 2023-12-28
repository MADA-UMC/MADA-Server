package com.umc.mada.custom.repository;

import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.WearingItem;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WearingItemRepository extends JpaRepository<WearingItem, Long> {
    List<WearingItem> findByUser(User user);
    @Query("select w.customItem from WearingItem w where w.user = :user")
    List<CustomItem> findCustomItemByUser(@Param("user") User user);
}
