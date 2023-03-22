package com.blog.bolgsearch.utils.searchutils;

import com.blog.bolgsearch.domain.BlogContents;
import com.blog.bolgsearch.dto.BlogContentsDTO;
import com.blog.bolgsearch.dto.KaKaoSearchResponseDTO;
import com.blog.bolgsearch.dto.SearchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // List<SearchUtil>로 Bean 주입 될 때 최 우선순위 보장 -> 카카오 API를 첫번째로 시도 후 실패 시 다른 API 호출
public class KaKaoSearchUtil implements SearchUtil{
    private final RestTemplate restTemplate;
    private final String API_URL;
    private final String API_KEY;
    private ResponseEntity<KaKaoSearchResponseDTO> response;

    public KaKaoSearchUtil(RestTemplate restTemplate
                            , @Value("${kakao.url}") String apiUrl
                            , @Value("${kakao.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.API_URL = apiUrl;
        this.API_KEY = apiKey;
    }

    @Override
    public BlogContentsDTO search(SearchDTO searchDTO) throws Exception {
        int page = searchDTO.getPage();
        int size = searchDTO.getContentsLength();
        String sort = searchDTO.getSort() == null ? SortValue.ACCURACY.getKakao() : searchDTO.getSort().getKakao();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);

        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("query", searchDTO.getQuery())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<KaKaoSearchResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, KaKaoSearchResponseDTO.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            List<KaKaoSearchResponseDTO.Document> documents = response.getBody().getDocuments();
            List<BlogContents> blogContents = documents.stream()
                    .map(document -> BlogContents.builder()
                            .title(document.getTitle())
                            .url(document.getUrl())
                            .contents(document.getContents())
                            .datetime(document.getDatetime())
                            .thumbnail(document.getThumbnail())
                            .blogName(document.getBlogname())
                            .build())
                    .collect(Collectors.toList());

            int contentsTotal = response.getBody().getMeta().getTotalCount();
            return BlogContentsDTO.builder()
                    .contentsTotal(contentsTotal)
                    .page(searchDTO.getPage())
                    .pageTotal(contentsTotal / searchDTO.getContentsLength() + 1) // 총 페이지 = 노출가능 문서 수 / size + 1
                    .contentsList(blogContents)
                    .build();
        } else {
            throw new Exception("[KaKaoSearchUtil] Failed search");
        }
    }

}
