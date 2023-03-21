package com.kakaobanktest.bolgsearch.api.blogsearch;

import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.ResponseDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j //삭제
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class BlogSearchController {
    private final BlogSearchService blogSearchService;

    @GetMapping
    public ResponseDTO search(SearchDTO searchDTO) throws Exception {
        blogSearchService.saveKeyword(searchDTO.getQuery()); //search장애 여부 상관없이 검색기록 count는 기록하도록 transaction분리
        BlogContentsDTO results = blogSearchService.search(searchDTO);
        return new ResponseDTO(HttpStatus.OK, true, "success", results);
    }


/*    @GetMapping("/popular")
    public ResponseDTO  getPopularKeywords() {
        List<KeywordDTO> keywords = blogSearchService.getPopularKeywords()
                .stream()
                .map(KeywordDTO::new)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK, true, "success", keywords);
    }*/

}
