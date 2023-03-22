package com.blog.bolgsearch.utils;

import com.blog.bolgsearch.dto.SearchDTO;
import org.springframework.web.util.UriComponentsBuilder;

public class CommonUtils {

    /**
     * searchUtil parameter validation
     * @param searchDTO
     */
    public static void validationSearchUtilParam(SearchDTO searchDTO) {
        //default value setting
        if (searchDTO.getPage() == null) {
            searchDTO.setPage(1);
        }
        if (searchDTO.getContentsLength() == null) {
            searchDTO.setContentsLength(10);
        }

        if (searchDTO.getPage() == 0) {
            throw new IllegalArgumentException("[Illegal Data Range] min page is 1");
        }
        if (searchDTO.getPage() > 50) {
            throw new IllegalArgumentException("[Illegal Data Range] max page is 50");
        }
        if (searchDTO.getContentsLength() == 0) {
            throw new IllegalArgumentException("[Illegal Data Range] min ContentsLength is 1");
        }
        if (searchDTO.getContentsLength() > 50) {
            throw new IllegalArgumentException("[Illegal Data Range] max ContentsLength is 50");
        }
    }
}
