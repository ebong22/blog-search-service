package com.kakaobanktest.bolgsearch.dto;

import com.kakaobanktest.bolgsearch.utils.searchutils.SortValue;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class SearchDTO {
    private String query;
    private Integer page;
    private Integer contentsLength;
    private SortValue sort;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setContentsLength(Integer contentsLength) {
        this.contentsLength = contentsLength;
    }
}
