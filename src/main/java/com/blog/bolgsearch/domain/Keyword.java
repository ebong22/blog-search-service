package com.blog.bolgsearch.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    private long count;

    public Keyword(String keyword) {
        this.keyword = keyword;
        this.count = 1; // keyword 생성 시 임의로 count 설정할 수 없도록 함
    }

    public void updateCount() {
        this.count += 1;
    }
}
