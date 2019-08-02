package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.Objects;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String URI_ARTICLES = "/articles";
    private static final String SEAN_NAME = "sean";
    private static final String SEAN_EMAIL = "sean@gmail.com";
    private static final String POBI_NAME = "pobi";
    private static final String POBI_EMAIL = "pobi@gmail.com";
    private static final String DEFAULT_PASSWORD = "Woowahan123!";
    private static final String TITLE = "title";
    private static final String CONTENT = "contents";
    private static final String COVER_URL = "coverUrl";
    private static final String UPDATED_TITLE = "updated_title";
    private static final String UPDATED_COVER_URL = "updated_coverUrl";
    private static final String UPDATED_CONTENT = "updated_content";
    private static final String DEFAULT_URL = "/";
    private static final String EDIT_URL = "/edit";
    private static final String LOGIN_UTL = "/login";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ARTICLE_PATTERN = ".*articles/";
    private static final String DELETE_PATTERN = ".*/";


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private static int SEAN_ARTICLE_ID;

    @BeforeEach
    void 게시글_작성() {
        userRepository.save(new User(SEAN_NAME, SEAN_EMAIL, DEFAULT_PASSWORD));
        userRepository.save(new User(POBI_NAME, POBI_EMAIL, DEFAULT_PASSWORD));

        getPostExchange(SEAN_EMAIL)
                .expectStatus().isFound()
                .expectBody().consumeWith(response -> {
            String path = Objects.requireNonNull(response.getResponseHeaders().getLocation()).getPath();
            int index = path.lastIndexOf(DEFAULT_URL);
            SEAN_ARTICLE_ID = Integer.parseInt(path.substring(index + 1));
        });

        getPostExchange(POBI_EMAIL)
                .expectStatus().isFound();
    }

    @Test
    void 게시글_조회() {
        statusWith(HttpMethod.GET, URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID
                , SEAN_EMAIL, DEFAULT_PASSWORD).isOk();
    }

    @Test
    void 게시글_수정_페이지_이동() {
        statusWith(HttpMethod.GET, URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID + EDIT_URL
                , POBI_EMAIL, DEFAULT_PASSWORD)
                .is3xxRedirection();
    }

    @Test
    void 게시글_수정() {
        getPutExchange(SEAN_EMAIL)
                .expectStatus().isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, ARTICLE_PATTERN + SEAN_ARTICLE_ID);
    }

    @Test
    void 다른_사람이_게시글_수정() {
        getPutExchange(POBI_EMAIL)
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 다른_사람이_게시글_삭제() {
        statusWith(HttpMethod.DELETE, URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID,
                POBI_EMAIL, DEFAULT_PASSWORD).is3xxRedirection();
    }

    @AfterEach
    void 게시글_삭제() {
        statusWith(HttpMethod.DELETE, URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID,
                SEAN_EMAIL, DEFAULT_PASSWORD).isFound()
                .expectHeader().valueMatches(HttpHeaders.LOCATION, DELETE_PATTERN);
        userRepository.deleteAll();
    }

    private StatusAssertions statusWith(HttpMethod httpMethod, String uri, String email, String password) {
        return webTestClient.method(httpMethod).uri(uri)
                .cookie(JSESSIONID, getResponseCookie(email, password).getValue())
                .exchange().expectStatus();
    }

    private ResponseCookie getResponseCookie(String email, String password) {
        return webTestClient.post().uri(LOGIN_UTL)
                .body(fromFormData(EMAIL, email)
                        .with(PASSWORD, password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .returnResult(ResponseCookie.class).getResponseCookies().getFirst(JSESSIONID);
    }

    private WebTestClient.ResponseSpec getPutExchange(String seanEmail) {
        return webTestClient.put().uri(URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID)
                .cookie(JSESSIONID, getResponseCookie(seanEmail, DEFAULT_PASSWORD).getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(TITLE, UPDATED_TITLE)
                        .with(COVER_URL, UPDATED_COVER_URL)
                        .with(CONTENT, UPDATED_CONTENT))
                .exchange();
    }

    private WebTestClient.ResponseSpec getPostExchange(String seanEmail) {
        return webTestClient.post().uri(URI_ARTICLES)
                .cookie(JSESSIONID, getResponseCookie(seanEmail, DEFAULT_PASSWORD).getValue())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(TITLE, TITLE)
                        .with(COVER_URL, COVER_URL)
                        .with(CONTENT, CONTENT))
                .exchange();
    }
}
