package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RootControllerTest {
    private static final String ARTICLE_DELIMITER
            = "<div role=\"tabpanel\" class=\"tab-pane fade in active\" id=\"tab-centered-1\">";
    private static String title = "TEST";
    private static String coverUrl = "https://img.com";
    private static String contents = "testtest";
    private static String categoryId = "1";

    private static final String name = "미스터코";
    private static final String email = "test@test.com";
    private static final String password = "123123123";

    @Autowired
    private WebTestClient webTestClient;

    private static long articleId;

    private String JSSESIONID;

    @BeforeEach
    void setUp() {
        if (JSSESIONID == null) {
            JSSESIONID = getJSSESIONID(name, email, password);
        }

        webTestClient.post()
                .uri("/articles/new")
                .cookie("JSESSIONID", JSSESIONID)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents)
                        .with("categoryId", categoryId))
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    articleId = Long.parseLong(s.split("/")[4]);
                });
    }

    @Test
    public void index() {
        int count = 1;

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, ARTICLE_DELIMITER));
                });
    }

    @Test
    public void indexTestByCategory() {
        int count = 1;

        webTestClient.get()
                .uri("/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, ARTICLE_DELIMITER));
                });
    }

    @AfterEach
    void tearDown() {
        //delete
        webTestClient.delete()
                .uri("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound();
    }

    private String getJSSESIONID(String name, String email, String password) {
        List<String> result = new ArrayList<>();

        webTestClient.post()
                .uri("/signup")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange();

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .value("Set-Cookie", cookie -> result.add(cookie));

        return Stream.of(result.get(0).split(";"))
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(""))
                .split("=")[1];
    }
}