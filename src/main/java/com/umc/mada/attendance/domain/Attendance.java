package com.umc.mada.attendance.domain;

import com.umc.mada.user.domain.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "count", nullable = false)
    private int count;

    @Builder
    public Attendance(Long id, int count) {
        this.id = id;
        this.count = count;
    }

    public void setCount(int i) {

    }

    public void setUser(User user) {

    }
}

