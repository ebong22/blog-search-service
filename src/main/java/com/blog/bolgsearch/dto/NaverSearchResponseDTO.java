package com.blog.bolgsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverSearchResponseDTO {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        private String title;

        private String link;

        private String description;

        @JsonProperty("bloggername")
        private String bloggerName;

        private String postdate;

        private String thumbnail;

    }
}
