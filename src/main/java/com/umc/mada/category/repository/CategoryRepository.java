package com.umc.mada.category.repository;

import com.umc.mada.category.domain.Category;
import com.umc.mada.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryById(Integer id);
}
