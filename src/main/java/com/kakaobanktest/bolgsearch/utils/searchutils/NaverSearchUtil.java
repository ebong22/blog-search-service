package com.kakaobanktest.bolgsearch.utils.searchutils;

import com.kakaobanktest.bolgsearch.domain.BlogContents;
import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.NaverSearchResponseDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobanktest.bolgsearch.utils.CommonUtils.getUrlString;

@Component
@Order(2)
public class NaverSearchUtil implements SearchUtil{

    private final RestTemplate restTemplate;
    private final String API_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    public NaverSearchUtil(RestTemplate restTemplate
                            , @Value("${naver.url}") String apiUrl
                            , @Value("${naver.client.id}") String clientId
                            , @Value("${naver.client.secret}") String clientSecret) {
        this.restTemplate = restTemplate;
        this.API_URL = apiUrl;
        this.CLIENT_ID = clientId;
        this.CLIENT_SECRET = clientSecret;
    }

    @Override
    public BlogContentsDTO search(SearchDTO searchDTO) throws Exception {
        String sort = searchDTO.getSort() == null ? SortValue.ACCURACY.getNaver() : searchDTO.getSort().getNaver();
        String url = getUrlString(API_URL, searchDTO, sort);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), NaverSearchResponseDTO.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            List<NaverSearchResponseDTO.Item> items = response.getBody().getItems();
            List<BlogContents> blogContents = items.stream()
                    .map(item -> BlogContents.builder()
                            .title(item.getTitle())
                            .url(item.getLink())
                            .contents(item.getDescription())
                            .datetime(item.getPostdate())
                            .thumbnail(item.getThumbnail())
                            .blogName(item.getBloggerName())
                            .build())
                    .collect(Collectors.toList());

            int contentsTotal = response.getBody().getTotal();
            return BlogContentsDTO.builder()
                    .contentsTotal(contentsTotal)
                    .page(searchDTO.getPage())
                    .pageTotal(contentsTotal / searchDTO.getContentsLength() + 1) // 총 페이지 = 전체 문서 수 / size + 1
                    .contentsList(blogContents)
                    .build();
        } else {
            throw new Exception("[NaverSearchUtil] Failed search");
        }
    }
}
