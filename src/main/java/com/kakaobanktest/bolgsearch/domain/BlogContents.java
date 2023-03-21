package com.kakaobanktest.bolgsearch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
//@AllArgsConstructor //@todo 삭제
public class BlogContents {
    private String title;
    private String url;
    private String contents;
    private String datetime;
    private String thumbnail;
    private String blogName;
}
