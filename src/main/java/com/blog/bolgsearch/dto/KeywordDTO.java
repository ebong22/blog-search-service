package com.blog.bolgsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class KeywordDTO {
    private String keyword;
    private long count;
}
