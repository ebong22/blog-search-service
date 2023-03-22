package com.blog.bolgsearch.api.search;

import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.KeywordDTO;
import com.blog.bolgsearch.dto.ResponseDTO;
import com.blog.bolgsearch.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.blog.bolgsearch.utils.CommonUtils.validationSearchUtilParam;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * 블로그 게시물 검색<br/>
     * 질의어를 통해 블로그 게시물 검색<br/>
     * 카카오 API로 검색 시도 후 장애 시 네이버 API를 통해 검색
     *
     * @param searchDTO
     * @return
     * @throws Exception
     */
    @GetMapping
    public ResponseDTO search(SearchDTO searchDTO) throws Exception {
        searchService.saveKeyword(searchDTO.getQuery()); //search 장애 여부 상관없이 검색기록 count는 기록하도록 transaction 분리

        validationSearchUtilParam(searchDTO);
        BlogContentsDTO results = searchService.search(searchDTO);

        return new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, true, "success", results);
    }

    /**
     * 인기 검색어 목록<br/>
     * 최대 10개의 키워드를 많이 검색한 순서대로 제공
     * @return
     */
    @GetMapping("/popular")
    public ResponseDTO  getPopularKeywords() {
        List<KeywordDTO> keywords = searchService.getPopularKeywords()
                .stream()
                .map(keyword -> new KeywordDTO(keyword.getKeyword(), keyword.getCount()))
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, true, "success", keywords);
    }
}
