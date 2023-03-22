package com.blog.bolgsearch.api.search;

import com.blog.bolgsearch.domain.BlogContents;
import com.blog.bolgsearch.domain.Keyword;
import com.blog.bolgsearch.domain.KeywordRepository;
import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.SearchDTO;
import com.blog.bolgsearch.utils.searchutils.KaKaoSearchUtil;
import com.blog.bolgsearch.utils.searchutils.NaverSearchUtil;
import com.blog.bolgsearch.utils.searchutils.SearchUtil;
import com.blog.bolgsearch.utils.searchutils.SortValue;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@Transactional
class SearchServiceImplTest {

    @Mock
    private KaKaoSearchUtil kaKaoSearchUtil;

    @Mock
    private NaverSearchUtil naverSearchUtil;

    @Mock
    private List<SearchUtil> searchUtils;

    @Autowired
    private KeywordRepository keywordRepository;

    private SearchServiceImpl searchService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        searchUtils = Arrays.asList(kaKaoSearchUtil, naverSearchUtil);
        searchService = new SearchServiceImpl(searchUtils, keywordRepository);
    }

    /**
     *  테스트케이스 <br/>
     *  첫번째 SearchUtil(kakao) : 정상작동<br/>
     *  나머지 SearchUtil(naver) : 정상작동<br/>
     *  기대 결과값 : 정상작동
     * @throws Exception
     */
    @Test
    void search() throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .query("kakao bank")
                .page(1)
                .contentsLength(10)
                .sort(SortValue.ACCURACY)
                .build();
        BlogContents kakaoBankContentsMock = getBlogContents("kakao bank test", "https://www.kakaobank.com");
        BlogContents naverContentsMock = getBlogContents("kakao bank test2", "https://www.naver.com");

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

        for (SearchUtil util : searchUtils) {
            when(util.search(searchDTO)).thenReturn(expectedResult);
        }

        BlogContentsDTO result = searchService.search(searchDTO);
        log.debug("result = {}", result);
        log.debug("expectedResult = {}", expectedResult);
        assertThat(result).isEqualTo(expectedResult);
    }

    /**
     *  테스트케이스 <br/>
     *  첫번째 SearchUtil(kakao) : 장애발생<br/>
     *  나머지 SearchUtil(naver) : 장애발생<br/>
     *  기대 결과값 : 장애발생
     * @throws Exception
     */
    @Test
    void searchEx() throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .query("kakao bank")
                .page(1)
                .contentsLength(10)
                .sort(SortValue.ACCURACY)
                .build();

        for (SearchUtil util : searchUtils) {
            when(util.search(searchDTO)).thenThrow(new IllegalStateException());
        }

        assertThatThrownBy(() -> searchService.search(searchDTO))
                .isInstanceOf(IllegalStateException.class);
    }

    /**
     *  테스트케이스 <br/>
     *  첫번째 SearchUtil(kakao) : 장애발생<br/>
     *  나머지 SearchUtil(naver) : 정상작동<br/>
     *  기대 결과값 : 정상작동
     * @throws Exception
     */
    //첫번째 SearchUtil(kakao)이 장애가 발생하면 다음 SearchUtil(naver)을 사용하여 search
    @Test
    void searchFirstUtilsEx() throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .query("kakao bank")
                .page(1)
                .contentsLength(10)
                .sort(SortValue.ACCURACY)
                .build();

        BlogContents kakaoBankContentsMock = getBlogContents("kakao bank test", "https://www.kakaobank.com");
        BlogContents naverContentsMock = getBlogContents("kakao bank test2", "https://www.naver.com");

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


        for (int i = 0; i < searchUtils.size(); i++) {
            SearchUtil util = searchUtils.get(i);
            if (i == 0) { //첫번째 SearchUtil은 exception 발생
                when(util.search(searchDTO)).thenThrow(new IllegalStateException());
            }
            if (i != 0) {
                when(util.search(searchDTO)).thenReturn(expectedResult);
            }
        }

        BlogContentsDTO result = searchService.search(searchDTO);
        log.debug("result = {}", result);
        log.debug("expectedResult = {}", expectedResult);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getPopularKeywords() {
        saveKeyword("kakao1", 1);
        saveKeyword("kakao2", 2);
        saveKeyword("kakao3", 3);
        saveKeyword("kakao4", 4);
        saveKeyword("kakao5", 5);

        List<Keyword> result = searchService.getPopularKeywords();

        assertThat(result.get(0).getKeyword()).isEqualTo("kakao5");
        assertThat(result.get(1).getKeyword()).isEqualTo("kakao4");
        assertThat(result.get(2).getKeyword()).isEqualTo("kakao3");
        assertThat(result.get(3).getKeyword()).isEqualTo("kakao2");
        assertThat(result.get(4).getKeyword()).isEqualTo("kakao1");
    }

    @Test
    void getPopularKeywordsOver10() {
        saveKeyword("kakao1", 1);
        saveKeyword("kakao2", 2);
        saveKeyword("kakao3", 3);
        saveKeyword("kakao4", 4);
        saveKeyword("kakao5", 5);
        saveKeyword("kakao6", 6);
        saveKeyword("kakao7", 7);
        saveKeyword("kakao8", 8);
        saveKeyword("kakao9", 9);
        saveKeyword("kakao10", 10);
        saveKeyword("kakao11", 11);

        List<Keyword> result = searchService.getPopularKeywords();

        assertThat(result.size()).isEqualTo(10);
        assertThat(result.get(0).getKeyword()).isEqualTo("kakao11");
        assertThat(result.get(1).getKeyword()).isEqualTo("kakao10");
        assertThat(result.get(2).getKeyword()).isEqualTo("kakao9");
        assertThat(result.get(3).getKeyword()).isEqualTo("kakao8");
        assertThat(result.get(4).getKeyword()).isEqualTo("kakao7");
        assertThat(result.get(5).getKeyword()).isEqualTo("kakao6");
        assertThat(result.get(6).getKeyword()).isEqualTo("kakao5");
        assertThat(result.get(7).getKeyword()).isEqualTo("kakao4");
        assertThat(result.get(8).getKeyword()).isEqualTo("kakao3");
        assertThat(result.get(9).getKeyword()).isEqualTo("kakao2");
    }

    private void saveKeyword(String keyword, int saveCount) {
        for (int i = 0; i < saveCount; i++) {
            searchService.saveKeyword(keyword);
        }
    }

    private BlogContents getBlogContents(String title, String url) {
        return BlogContents.builder()
                .title(title)
                .url(url)
                .build();
    }
}