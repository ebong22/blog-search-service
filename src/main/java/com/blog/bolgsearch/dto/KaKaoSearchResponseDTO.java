package com.blog.bolgsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KaKaoSearchResponseDTO {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Meta {
        @JsonProperty("total_count")
        private Integer totalCount;

        @JsonProperty("pageable_count")
        private Integer pageableCount;

        @JsonProperty("is_end")
        private Boolean isEnd;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Document {
        private String title;
        private String contents;
        private String url;
        private String datetime;
        private String thumbnail;
        private String blogname;
    }
}
