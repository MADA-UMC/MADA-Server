package com.umc.mada.calendar.domain;


import com.umc.mada.global.BaseEntity;
import com.umc.mada.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Calendar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @Column(name = "user_id")
    private User user_id;
    @Column
    private String calender_name;
    @Column
    private Timestamp start_date;
    @Column
    private Timestamp end_date;
    @Column
    private String repeat;
    @Column
    private String d_day;
    @Column
    private String memo;
}
