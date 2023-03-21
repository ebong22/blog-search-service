package com.kakaobanktest.bolgsearch.utils.searchutils;

import lombok.Getter;

@Getter
public enum SortValue {
    ACCURACY("accuracy", "sim")
    , RECENCY("recency", "date");

    private final String kakao;
    private final String naver;

    private SortValue (String kakao, String naver) {
        this.kakao = kakao;
        this.naver = naver;
    }
}
