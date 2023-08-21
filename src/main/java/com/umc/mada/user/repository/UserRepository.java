package com.umc.mada.user.repository;

import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findByAuthId(String authId);
    Optional<User> findUserByNickname(String nickName);

}
