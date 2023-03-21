package com.kakaobanktest.bolgsearch.utils;

public class CommonUtils {

    /**
     * searchUtil parameter validation
     * @param page
     * @param size
     */
    public static void validationSearchUtilParam(int page, int size) {
        if (page > 50) {
            throw new IllegalArgumentException("[Illegal page] max page is 50");
        }
        if (size > 50) {
            throw new IllegalArgumentException("[Illegal ContentsLength] max ContentsLength is 50");
        }
    }
}
