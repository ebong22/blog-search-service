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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static com.kakaobanktest.bolgsearch.utils.CommonUtils.validationSearchUtilParam;

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
        //@TODO  page, size api 문서에 맞게 제한사항 추가
        // default value
        int page = searchDTO.getPage() == null ? 1 : searchDTO.getPage();
        int size = searchDTO.getContentsLength() == null ? 10 : searchDTO.getContentsLength();
        String sort = searchDTO.getSort() == null ? SortValue.ACCURACY.getNaver() : searchDTO.getSort().getNaver();
        validationSearchUtilParam(page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("query", searchDTO.getQuery())
                .queryParam("start", (page - 1) * size + 1)
                .queryParam("display", size)
                .queryParam("sort", sort);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponseDTO> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                NaverSearchResponseDTO.class
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
                    .page(page)
                    .pageTotal(contentsTotal / size + 1) // 총 페이지 = 전체 문서 수 / size + 1
                    .contentsList(blogContents)
                    .build();
        } else {
            throw new Exception("[NaverSearchUtil] Failed search");
        }
    }
}
