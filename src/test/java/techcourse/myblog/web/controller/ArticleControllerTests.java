package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests extends AuthedWebTestClient {
    private static final String URI_ARTICLES = "/articles";
    private static final String SEAN_NAME = "sean";
    private static final String SEAN_EMAIL = "sean@gmail.com";
    private static final String POBI_NAME = "pobi";
    private static final String POBI_EMAIL = "pobi@gmail.com";
    private static final String DEFAULT_PASSWORD = "Woowahan123!";
    private static final String DEFAULT_URL = "/";
    private static final String EDIT_URL = "/edit";
    private static final String ARTICLE_PATTERN = ".*articles/";
    private static final String DELETE_PATTERN = ".*/";

    @Autowired
    private UserRepository userRepository;

    private static int SEAN_ARTICLE_ID;

    @BeforeEach
    void 게시글_작성() {
        userRepository.save(new User(SEAN_NAME, SEAN_EMAIL, DEFAULT_PASSWORD));
        userRepository.save(new User(POBI_NAME, POBI_EMAIL, DEFAULT_PASSWORD));

        post(SEAN_EMAIL)
                .expectBody()
                .consumeWith(response -> {
                    String path = Objects.requireNonNull(response.getResponseHeaders().getLocation()).getPath();
                    int index = path.lastIndexOf(DEFAULT_URL);
                    SEAN_ARTICLE_ID = Integer.parseInt(path.substring(index + 1));
                });

        post(POBI_EMAIL);
    }

    @Test
    void 게시글_조회() {
        get(URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID
                , SEAN_EMAIL, DEFAULT_PASSWORD)
                .isOk();
    }

    @Test
    void 게시글_수정_페이지_이동() {
        get(URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID + EDIT_URL
                , POBI_EMAIL, DEFAULT_PASSWORD)
                .is3xxRedirection();
    }

    @Test
    void 게시글_수정() {
        put(SEAN_EMAIL, URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID)
                .expectStatus()
                .isFound()
                .expectHeader()
                .valueMatches(HttpHeaders.LOCATION, ARTICLE_PATTERN + SEAN_ARTICLE_ID);
    }

    @Test
    void 다른_사람이_게시글_수정() {
        put(POBI_EMAIL, URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID)
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 다른_사람이_게시글_삭제() {
        delete(URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID,
                POBI_EMAIL, DEFAULT_PASSWORD)
                .is3xxRedirection();
    }

    @AfterEach
    void 게시글_삭제() {
        delete(URI_ARTICLES + DEFAULT_URL + SEAN_ARTICLE_ID,
                SEAN_EMAIL, DEFAULT_PASSWORD).isFound()
                .expectHeader()
                .valueMatches(HttpHeaders.LOCATION, DELETE_PATTERN);

        userRepository.deleteAll();
    }
}
