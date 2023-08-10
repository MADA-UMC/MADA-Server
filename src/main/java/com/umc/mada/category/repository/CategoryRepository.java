package com.umc.mada.category.repository;

import com.umc.mada.category.domain.Category;
import com.umc.mada.file.domain.File;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findCategoriesByUserId(User userId);
    Optional<Category> deleteCategoryByUserIdAndId(User userId, int id);
    Optional<Category> findCategoryByUserIdAndId(User userId, int id);
}
