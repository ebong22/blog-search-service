package com.blog.bolgsearch.api.search;

import com.blog.bolgsearch.domain.Keyword;
import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.SearchDTO;

import java.util.List;

public interface SearchService {
    BlogContentsDTO search(SearchDTO searchDTO) throws Exception;
    public List<Keyword> getPopularKeywords();
    public void saveKeyword(String query);
}
