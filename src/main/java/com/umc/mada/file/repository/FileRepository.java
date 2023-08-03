package com.umc.mada.file.repository;

import com.umc.mada.file.domain.File;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    Optional<File> findFileById(Integer id);
}
