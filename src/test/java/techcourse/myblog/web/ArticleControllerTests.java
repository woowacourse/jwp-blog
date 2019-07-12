package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final int ARTICLE_ID = 0;
    private static final String LOCATION = "location";
    
    @Autowired
    private WebTestClient webTestClient;
    
    @BeforeEach
    void 게시글_작성_확인() {
        checkRedirect(HttpMethod.POST, "/articles", HttpStatus.FOUND,
                LOCATION, ".*articles.*");
    }
    
    @Test
    void index_페이지_조회() {
        check(HttpMethod.GET, "/", HttpStatus.OK);
    }
    
    @Test
    void 게시글_작성_페이지_확인() {
        check(HttpMethod.GET, "/writing", HttpStatus.OK);
    }
    
    @Test
    void 게시글_조회() {
        check(HttpMethod.GET, "/articles/" + ARTICLE_ID, HttpStatus.OK);
    }
    
    @Test
    void 게시글_수정_페이지_확인() {
        check(HttpMethod.GET, "/articles/" + ARTICLE_ID + "/edit", HttpStatus.OK);
    }
    
    @Test
    void 게시글_수정_확인() {
        checkRedirect(HttpMethod.PUT, "/articles/" + ARTICLE_ID, HttpStatus.FOUND,
                LOCATION, ".*articles.*");
    }
    
    @AfterEach
    void 게시글_삭제_확인() {
        checkRedirect(HttpMethod.DELETE, "/articles/" + ARTICLE_ID, HttpStatus.FOUND,
                LOCATION, ".*/");
    }
    
    private ResponseSpec check(HttpMethod httpMethod, String uri, HttpStatus httpStatus) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .exchange()
                .expectStatus().isEqualTo(httpStatus);
    }
    
    private ResponseSpec checkRedirect(HttpMethod httpMethod, String uri,
                                       HttpStatus httpStatus, String name, String pattern) {
        return check(httpMethod, uri, httpStatus)
                .expectHeader().valueMatches(name, pattern);
    }
}
