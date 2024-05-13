package com.umc.mada.my.domain;

import lombok.*;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="SAYING")
public class My {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "sayer")
    private String sayer;

    @Builder
    public My(Long id, String content, String sayer) {
        this.id = id;
        this.content = content;
        this.sayer = sayer;
    }
}
