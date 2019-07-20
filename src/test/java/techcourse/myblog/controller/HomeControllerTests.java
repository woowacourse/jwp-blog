package techcourse.myblog.controller;

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
public class HomeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 홈페이지_이동_확인(){
        checkResponseStatus(HttpMethod.GET, "/", HttpStatus.OK);
    }

    @Test
    void 게시글_작성_페이지_확인() {
        checkResponseStatus(HttpMethod.GET, "/writing", HttpStatus.OK);
    }

    @Test
    void 로그인_페이지_확인() {
        checkResponseStatus(HttpMethod.GET, "/login", HttpStatus.OK);
    }

    @Test
    void 회원가입_페이지_확인() {
        checkResponseStatus(HttpMethod.GET, "/signup", HttpStatus.OK);
    }

    private ResponseSpec check(HttpMethod httpMethod, String uri) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .exchange();
    }

    private ResponseSpec checkResponseStatus(HttpMethod httpMethod, String uri, HttpStatus httpStatus) {
        return check(httpMethod, uri)
                .expectStatus().isEqualTo(httpStatus);
    }

    private ResponseSpec checkResponseHeaderLocation(HttpMethod httpMethod, String uri,
                                                                   HttpStatus httpStatus, String name, String pattern) {
        return checkResponseStatus(httpMethod, uri, httpStatus)
                .expectHeader().valueMatches(name, pattern);
    }
}
