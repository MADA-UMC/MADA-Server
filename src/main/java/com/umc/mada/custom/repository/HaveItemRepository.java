package com.umc.mada.custom.repository;

import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HaveItemRepository extends JpaRepository<HaveItem, Long> {
//    Optional<HaveItem> findByCustomItemAndUser(CustomItem customItem, User user);
    Optional<HaveItem> findById(Long id);
    Optional<HaveItem> findByIdAndUser(Long id, User user);
    List<HaveItem> findByUser(User user);
    List<HaveItem> findByUserAndWearing(User user, boolean wearing);
}
