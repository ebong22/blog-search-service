# 블로그 검색 서비스  blog-search-service 

키워드로 블로그 검색을 할 수 있는 서비스입니다.   
카카오 블로그 검색 API를 기본으로 사용하며 장애가 발생한 경우,
네이버 블로그 검색 API를 통해 데이터를 제공합니다.
* API 명세 : https://ebong.notion.site/634c0230154942dab6b8a7e64beac63e
* jar 다운로드 : https://github.com/ebong22/blog-search-service/releases/download/v1.0/bolg-search-0.0.1-SNAPSHOT.jar
```
wget https://github.com/ebong22/blog-search-service/releases/download/v1.0/bolg-search-0.0.1-SNAPSHOT.jar # 다운로드
java -jar ./bolg-search-0.0.1-SNAPSHOT.jar # 실행
```


## 개발 환경
* Java17
* Spring Boot 3.0.4
* JPA
* H2 DB

## 접속 정보
* port : 80   
* h2 DB 콘솔 : http://localhost/h2-console   

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

## 추가 사항
### 사용 라이브러리
* Lombok : 개발 생산성 향상 및 간결한 코드 작성을 위해 사용

### 추가 구현 사항
Spring Aop를 활용하여 메서드 시작 종료 시점에 log 기록
* 적용 목적 : 실제 운영환경에서의 용이한 디버깅을 위해
* aop 적용 대상 : api, utils 하위 패키지에 있는 클래스 전체
