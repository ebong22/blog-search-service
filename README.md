# 블로그 검색 서비스  blog-search-service 

키워드로 블로그 검색을 할 수 있는 서비스입니다.   
카카오 블로그 검색 API를 기본으로 사용하며 장애가 발생한 경우,
네이버 블로그 검색 API를 통해 데이터를 제공합니다.
<br/><br/>
* API 명세 : https://ebong.notion.site/634c0230154942dab6b8a7e64beac63e
* jar 다운로드 : https://github.com/ebong22/blog-search-service/releases/download/v1.0/bolg-search-0.0.1-SNAPSHOT.jar
```
wget https://github.com/ebong22/blog-search-service/releases/download/v1.0/bolg-search-0.0.1-SNAPSHOT.jar # 다운로드
java -jar ./bolg-search-0.0.1-SNAPSHOT.jar # 실행
```

<br/><br/>
## 개발 환경
* Java17
* Spring Boot 3.0.4
* JPA
* H2 DB

<br/><br/>
## 접속 정보
* port : 80   
* h2 DB 콘솔 : http://localhost/h2-console  

<br/><br/>
## docker
### image pull
* x86_64
```
docker pull ebong/blog-search-x86_64
```
* arm64
```
docker pull ebong/blog-search-arm64
```

### image build
* x86_64
```
docker build --platform=linux/amd64 -t {imageName} .
```
* arm64
```
docker build --platform=linux/arm64/v8 -t {imageName} .
```

<br/><br/>
## 추가 사항
### 사용 라이브러리
* Lombok : 개발 생산성 향상 및 간결한 코드 작성을 위해 사용


### 추가 구현 사항
__1. 카카오 API 장애 시 네이버 API를 통해 검색__   
&emsp;&emsp;다형성을 활용하여 다양한 검색 API를 추가할 수 있도록 설계 (해당 프로젝트에는 카카오, 네이버 API가 구현되어 있음)
  - `SearchUtil` inteface의 `search(SearchDTO searchDTO)` 메서드를 구현 후 Spring Bean으로 추가하여 검색 API 확장 가능
```
public interface SearchUtil {
    BlogContentsDTO search(SearchDTO searchDTO) throws Exception;
}
```
  - Service(`SearchServiceImpl`)에서 `SearchUtil`을 구현한 모든 Bean을 List로 주입받아 사용
```
@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final List<SearchUtil> searchUtils;
    
    // more...
```

  - 카카오 API를 사용하는 KaKaoSearchUtil에는 `@Order(Ordered.HIGHEST_PRECEDENCE)` 애노테이션을 통해   
`private final List<SearchUtil> searchUtils;`으로 주입받을 시 가장 첫 번째 index를 보장받음   
따라서 카카오 API를 가장 처음으로 시도 후 `@Order(n)` 애노테이션의 우선순위에 따라 구현된 다른 검색 API util을 통해 검색을 시도함
```
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class KaKaoSearchUtil implements SearchUtil{ // more...
```
<br/><br/>
__2. Spring Aop를 활용하여 메서드 시작 종료 시점에 log 기록 (`LogAspect`)__
  * 적용 목적 : 실제 운영환경에서의 용이한 디버깅을 위해 적용
  * aop 적용 대상 : api, utils 하위 패키지에 있는 클래스 전체

