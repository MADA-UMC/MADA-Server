package com.umc.mada.user.repository;

import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findByAuthId(String authId);
    Optional<User> findByAuthIdAndAccountExpired(String authId, Boolean accountExpired);
    Optional<User> findUserByNickname(String nickName);

}
