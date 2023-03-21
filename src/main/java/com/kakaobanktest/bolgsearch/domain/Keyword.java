package com.kakaobanktest.bolgsearch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    private long count;

    public Keyword(String keyword) {
        this.keyword = keyword;
        this.count = 1;
    }

    public void updateCount() {
        this.count += 1;
    }
}
