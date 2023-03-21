package com.kakaobanktest.bolgsearch.api.blogsearch;

import com.kakaobanktest.bolgsearch.domain.Keyword;
import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;

import java.util.List;

public interface BlogSearchService {
    BlogContentsDTO search(SearchDTO searchDTO) throws Exception;
    public List<Keyword> getPopularKeywords();
    public void saveKeyword(String query);
}
