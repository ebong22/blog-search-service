package com.blog.bolgsearch.api.search;

import com.blog.bolgsearch.domain.Keyword;
import com.blog.bolgsearch.domain.KeywordRepository;
import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.SearchDTO;
import com.blog.bolgsearch.utils.searchutils.SearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final List<SearchUtil> searchUtils; // List<SearchUtil> -> 카카오 API를 첫번째로 시도 후 실패 시 다른 API 호출을 위해 List로 주입
    private final KeywordRepository keywordRepository;

    /**
     * 블로그 게시물 검색<br/>
     * 질의어를 통해 블로그 게시물 검색<br/>
     * 카카오 API로 검색 시도 후 장애 시 네이버 API를 통해 검색
     * @param searchDTO
     * @return
     * @throws Exception
     */
    @Override
    public BlogContentsDTO search(SearchDTO searchDTO) throws Exception{
        Exception exHolder = null;
        for (SearchUtil util : searchUtils) {
            try {
                BlogContentsDTO result = util.search(searchDTO);
                return result;
            } catch (Exception e) {
                exHolder = e;
            }
        }
        throw exHolder;
    }

    /**
     * 인기 검색어 목록<br/>
     * 최대 10개의 키워드를 많이 검색한 순서대로 제공
     * @return
     */
    @Override
    public List<Keyword> getPopularKeywords() {
        return keywordRepository.findTop10ByOrderByCountDesc();
    }

    /**
     * 키워드 저장<br/>
     * 처음 검색 된 키워드면 insert를 아니면 count + 1로 update
     * @param query
     */
    @Override
    public void saveKeyword(String query) {
        if (!query.equals("")) {
            Keyword keyword = keywordRepository.findByKeyword(query);
            if (keyword != null) {
                keyword.updateCount();
            } else {
                keyword = new Keyword(query);
            }
            keywordRepository.save(keyword);
        }
    }
}
