package com.blog.bolgsearch.utils.searchutils;

import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.SearchDTO;

public interface SearchUtil {
    BlogContentsDTO search(SearchDTO searchDTO) throws Exception;
}
