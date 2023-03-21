package com.kakaobanktest.bolgsearch.api.blogsearch;

import com.kakaobanktest.bolgsearch.domain.Keyword;
import com.kakaobanktest.bolgsearch.domain.KeywordRepository;
import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;
import com.kakaobanktest.bolgsearch.utils.searchutils.SearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogSearchServiceImpl implements BlogSearchService{

    private final List<SearchUtil> searchUtils;
    private final KeywordRepository keywordRepository;

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

    //들여쓰기 //sevice에 추가
    @Override
    public List<Keyword> getPopularKeywords() {
        return keywordRepository.findTop10ByOrderByCountDesc();
    }

    //들여쓰기
    @Override
    public void saveKeyword(String query) {
        Keyword keyword = keywordRepository.findByKeyword(query);
        if (keyword != null) {
            keyword.updateCount();
        } else {
            keyword = new Keyword(query);
        }
        keywordRepository.save(keyword);
    }
}
