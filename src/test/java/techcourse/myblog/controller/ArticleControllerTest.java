package techcourse.myblog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static final String TITLE = "목적의식 있는 연습을 통한 효과적인 학습";
    private static final String COVER_URL = "https://scontent-icn1-1.xx.fbcdn.net/v/t1.0-9/1514627_858869820895635_1119508450771753991_n.jpg?_nc_cat=110&_nc_ht=scontent-icn1-1.xx&oh=a32aa56b750b212aee221da7e9711bb1&oe=5D8781A4";
    private static final String CONTENTS = "나는 우아한형제들에서 우아한테크코스(이하 우테코) 교육 과정을 진행하고 있다. 우테코를 설계하면서 고민스러웠던 부분 중의 하나는 ‘선발 과정을 어떻게 하면 의미 있는 시간으로 만들 것인가?’였다.";
    private static final User TEST_USER = new User("name", "email@email.com", "passWORD!1");

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleForm() {
        webTestClient.get().uri("/articles/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("새로운 article이 생성된다.")
    void createTest() {
        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("title", TITLE)
                        .with("coverUrl", COVER_URL)
                        .with("contents", CONTENTS)
                        .with("author", TEST_USER.getEmail()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getRequestHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                try {
                                    String body = new String(redirectResponse.getResponseBody(), "UTF-8");
                                    assertThat(body).contains(TITLE);
                                    assertThat(body).contains(COVER_URL);
                                    assertThat(body).contains(CONTENTS);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            });
                });
    }
}
