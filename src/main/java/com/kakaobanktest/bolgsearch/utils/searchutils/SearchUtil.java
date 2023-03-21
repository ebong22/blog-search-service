package com.kakaobanktest.bolgsearch.utils.searchutils;

import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;

public interface SearchUtil {
    BlogContentsDTO search(SearchDTO searchDTO) throws Exception;
}
