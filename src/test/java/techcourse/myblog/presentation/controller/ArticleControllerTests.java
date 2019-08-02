package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static techcourse.myblog.presentation.controller.ControllerTestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private String port;

    private UserDto userDto;
    private LoginDto loginDto;
    private String sessionId;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setName("test");
        userDto.setPassword("password");

        loginDto = new LoginDto();
        loginDto.setEmail(userDto.getEmail());
        loginDto.setPassword(userDto.getPassword());
    }

    @Test
    void 로그인_후_게시글_작성_페이지_이동() {
        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                webTestClient.get()
                        .uri("/writing" + sessionId)
                        .exchange()
                        .expectStatus().isOk();
            });
        });
    }

    @Test
    void 게시글_저장() {
        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                webTestClient.post()
                        .uri("/articles" + sessionId)
                        .body(BodyInserters.fromFormData("title", "article test")
                                .with("coverUrl", "coverUrl")
                                .with("contents", "contents"))
                        .exchange()
                        .expectStatus().is3xxRedirection()
                        .expectBody().consumeWith(addArticle -> {
                            articleView(webTestClient, sessionId, 3L, articleView -> {
                                assertTrue(new String(articleView.getResponseBody()).contains("article test"));
                            });
                });
            });
        });
    }

    @Test
    void 게시글_업데이트() {
        initialWork(result -> {
            webTestClient.put()
                    .uri("/articles/" + 2 + sessionId)
                    .body(BodyInserters.fromFormData("title", "update title")
                            .with("coverUrl", "update coverUrl")
                            .with("contents", "update contents"))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectBody().consumeWith(updateArticle -> {
                        articleView(webTestClient, sessionId, 2L, articleView -> {
                            assertTrue(new String(articleView.getResponseBody()).contains("update title"));
                        });
            });
        });
    }

    @Test
    void 게시글_삭제하기() {
        initialWork(result -> {
            webTestClient.delete()
                    .uri("/articles/" + 1 + sessionId)
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectHeader().valueMatches("Location", "http://localhost:" + port + "/.*");
        });
    }

    private void initialWork(Consumer<EntityExchangeResult<byte[]>> result) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("test title");
        articleDto.setCoverUrl("test coverUrl");
        articleDto.setContents("test contents");

        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                sessionId = getSessionId(postResponseLogin);
                createArticle(webTestClient, articleDto, sessionId, result);
            });
        });
    }
}
