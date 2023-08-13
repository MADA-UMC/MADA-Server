package com.umc.mada.saying.domain;

import lombok.*;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Saying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    private String content;
    @Column(name = "sayer")
    private String sayer;

    @Builder
    public Saying(Long id, String content, String sayer) {
        this.id = id;
        this.content = content;
        this.sayer = sayer;
    }
}
