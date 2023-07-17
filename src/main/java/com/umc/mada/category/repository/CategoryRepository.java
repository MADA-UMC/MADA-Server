package com.umc.mada.category.repository;

import com.umc.mada.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // JpaRepository를 상속받아 기본적인 CRUD(Create, Read, Update, Delete) 기능을 지원
}
