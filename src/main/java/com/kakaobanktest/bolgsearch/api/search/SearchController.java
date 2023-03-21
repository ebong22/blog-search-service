package com.kakaobanktest.bolgsearch.api.search;

import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.KeywordDTO;
import com.kakaobanktest.bolgsearch.dto.ResponseDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;
import com.kakaobanktest.bolgsearch.utils.searchutils.KaKaoSearchUtil;
import com.kakaobanktest.bolgsearch.utils.searchutils.NaverSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseDTO search(SearchDTO searchDTO) throws Exception {
        searchService.saveKeyword(searchDTO.getQuery()); //search장애 여부 상관없이 검색기록 count는 기록하도록 transaction분리
        BlogContentsDTO results = searchService.search(searchDTO);
        return new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, true, "success", results);
    }

    @GetMapping("/popular")
    public ResponseDTO  getPopularKeywords() {
        List<KeywordDTO> keywords = searchService.getPopularKeywords()
                .stream()
                .map(keyword -> new KeywordDTO(keyword.getKeyword(), keyword.getCount()))
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, true, "success", keywords);
    }
}
