package com.kakaobanktest.bolgsearch.dto;

import com.kakaobanktest.bolgsearch.domain.BlogContents;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogContentsDTO {
    private Integer contentsTotal;
    private Integer page;
    private Integer pageTotal;
    private List<BlogContents> contentsList;
}
