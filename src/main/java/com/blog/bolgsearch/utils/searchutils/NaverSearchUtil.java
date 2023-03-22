package com.blog.bolgsearch.utils.searchutils;

import com.blog.bolgsearch.domain.BlogContents;
import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.NaverSearchResponseDTO;
import com.blog.bolgsearch.dto.SearchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


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
        int page = searchDTO.getPage();
        int size = searchDTO.getContentsLength();
        String sort = searchDTO.getSort() == null ? SortValue.ACCURACY.getNaver() : searchDTO.getSort().getNaver();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);

        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("query", searchDTO.getQuery())
                .queryParam("start", (page - 1) * size + 1)
                .queryParam("display", size)
                .queryParam("sort", sort)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, NaverSearchResponseDTO.class
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
