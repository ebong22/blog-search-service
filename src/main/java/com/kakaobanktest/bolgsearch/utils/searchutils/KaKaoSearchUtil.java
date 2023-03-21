package com.kakaobanktest.bolgsearch.utils.searchutils;

import com.kakaobanktest.bolgsearch.domain.BlogContents;
import com.kakaobanktest.bolgsearch.dto.BlogContentsDTO;
import com.kakaobanktest.bolgsearch.dto.KaKaoSearchResponseDTO;
import com.kakaobanktest.bolgsearch.dto.SearchDTO;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
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
        //@TODO  page, size api 문서에 맞게 제한사항 추가
        // default value
        int page = searchDTO.getPage() == null ? 1 : searchDTO.getPage();
        int size = searchDTO.getContentsLength() == null ? 10 : searchDTO.getContentsLength();
        String sort = searchDTO.getSort().getKakao();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + API_KEY);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("query", searchDTO.getQuery())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<KaKaoSearchResponseDTO> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                KaKaoSearchResponseDTO.class
        );

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
            return BlogContentsDTO.builder()
                    .contentsTotal(response.getBody().getMeta().getTotalCount())
                    .page(page)
                    .pageTotal(response.getBody().getMeta().getPageableCount() / size + 1) // 총 페이지 = 노출가능 문서 수 / size + 1
                    .contentsList(blogContents)
                    .build();
        } else {
            throw new Exception("[KaKaoSearchUtil] Failed search");
        }
    }
}
