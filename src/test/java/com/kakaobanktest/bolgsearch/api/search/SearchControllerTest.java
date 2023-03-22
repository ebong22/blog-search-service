package com.kakaobanktest.bolgsearch.api.search;

import com.kakaobanktest.bolgsearch.domain.BlogContents;
import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.ResponseDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch() throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .query("kakao")
                .build();

        BlogContents kakaoBankContentsMock = getBlogContents("kakao bank test", "https://www.kakaobank.com");
        BlogContents naverContentsMock = getBlogContents("kakao test", "https://www.naver.com");

        List<BlogContents> blogContentsList = Arrays.asList(
                kakaoBankContentsMock
                , naverContentsMock
        );

        BlogContentsDTO expectedResult = BlogContentsDTO.builder()
                .contentsTotal(blogContentsList.size())
                .page(1)
                .pageTotal(1)
                .contentsList(blogContentsList)
                .build();
        when(searchService.search(searchDTO)).thenReturn(expectedResult);

        ResponseDTO result = searchController.search(searchDTO);
        BlogContentsDTO resultData = (BlogContentsDTO) result.getData();

        log.debug("result = {}", result);
        log.debug("expectedResult = {}", expectedResult);
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getSuccess()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo("success");
        assertThat(resultData.getContentsTotal()).isEqualTo(expectedResult.getContentsTotal());
        assertThat(resultData.getPage()).isEqualTo(expectedResult.getPage());
        assertThat(resultData.getPageTotal()).isEqualTo(expectedResult.getPageTotal());
        assertThat(resultData.getContentsList()).isEqualTo(expectedResult.getContentsList());
    }


    @AllArgsConstructor
    @Getter
    private static class SearchResult {
        private final String title;
        private final String url;
    }

    private BlogContents getBlogContents(String title, String url) {
        return BlogContents.builder()
                .title(title)
                .url(url)
                .build();
    }

}