package com.umc.mada.timetable.repository;

import com.umc.mada.timetable.domain.Comment;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findCommentByUserIdAndDateIs(User userId, LocalDate date);
}
