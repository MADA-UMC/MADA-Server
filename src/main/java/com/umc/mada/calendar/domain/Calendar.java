package com.umc.mada.calendar.domain;


import com.umc.mada.global.BaseEntity;
import com.umc.mada.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long uid;
    @Column(name = "calender_name")
    private String calenderName;
    @Column(name = "color")
    private String color;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "repeat")
    private String repeat;
    @Column(name = "d_day")
    private Character d_day;
    @Column(name = "memo")
    private String memo;
}
