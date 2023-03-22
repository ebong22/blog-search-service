package com.kakaobanktest.bolgsearch.utils;

import com.kakaobanktest.bolgsearch.dto.SearchDTO;
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

    /**
     * API 요청 url 생성
     * @param apiUrl
     * @param searchDTO
     * @param sort
     * @return
     */
    public static String getUrlString(String apiUrl, SearchDTO searchDTO, String sort) {
        return UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("query", searchDTO.getQuery())
                .queryParam("page", searchDTO.getPage())
                .queryParam("size", searchDTO.getContentsLength())
                .queryParam("sort", sort)
                .toUriString();
    }
}
