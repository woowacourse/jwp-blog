# 나만의 블로그 서비스

## Spring Boot으로 게시글 CRUD

## 필요 기능

1. index 서빙  
    1.1 모든 Article을 동적으로 표현  
    1.2 해당 Article 조회 (GET /articles/ + ID)
    1.3 Article 생성 진입점 (GET /writing) 
2. Article 생성 (/writing)  
    2.1 POST /articles/new
    
3. Article 수정  
    3.1 수정 템플릿 제공 (GET /articles/ + ID + /edit)
    3.2 PUT /articles/ + ID
    
4. Article 삭제
    4.1 Article (/ariticles/ + ID) 에서 DELETE 요청
    4.2 DELETE /articles/ + ID
    
## 추가사항

- HTML 중복 제거 -> thymeleaf fragment
- 클래스, 스태틱 파일 수정시 자동 재시작 -> spring-boot-devtools
- thymeleaf