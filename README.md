## My Blog 만들기 프로젝트
### 기능 구현 목록
1. 환경설정
    * 정적 파일의 경로 설정
        * reference가 기본 경로를 참조하도록 수정
    * 정적 파일 수정 시 재시작하지 않고 변경사항이 반영하도록 설정
        * gradle에 spring-boot-devtools dependency 추가

2. Article class 구현
    * 게시물의 정보를 담고 있는 bean 객체
    * articleId, title, content, writer, picture source, write_date

3. 게시글 작성
    * 작성된 글을 POST 방식으로 전달
    * 전달받은 게시글의 정보를 List\<Article>에 저장

4. 전체 글 목록 조회
    * List\<Article>에 저장된 전체 글 목록을 가져옴
    * 가져온 글 목록을 웹 UI에 출력

 5. 게시글 개별 조회
    * 선택된 articleId를 이용해 해당 Article 객체만 가져옴
    * 가져온 Article 객체를 웹 UI에 출력

 6. 게시글 수정
    * 선택된 articleId와 userId를 이용해 해당 Article 정보를 가져와 수정 웹 UI에 출력
    * 해당 Article 객체가 존재하지 않을 경우 예외 처리
    * 해당 Article 객체의 writer와 userId가 일치하지 않을 경우 예외 처리
    * 기존의 List<Article>에서 Article 객체를 삭제하고 수정한 Article 객체를 추가
    * 수정된 Article 객체를 가져와 웹 UI에 출력

 7. 게시글 삭제
    * 해당 Article 객체가 존재하지 않을 경우 예외 처리
    * 해당 Article 객체의 writer와 userId가 일치하지 않을 경우 예외 처리
    * 선택된 articleId를 이용해 해당 Article 삭제
    * List\<Article>에 저장된 전체 글 목록을 가져옴
 8. 게시글의 articleId를 생성하는 메서드 작성
        * List\<Article>의 가장 마지막 articleId보다 1만큼 큰 Id를 반환