package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.UserRepository;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private Long id = 0L;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test() {
        UserDto userDto = new UserDto();
        userDto.setName("zino");
        userDto.setEmail("email2@zino.me");
        userDto.setPassword("password");

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(userDto.getEmail());
        loginDto.setPassword(userDto.getPassword());

        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("title");
        articleDto.setCoverUrl("coverUrl");
        articleDto.setContents("contetnts");

        CommentDto commentDto = new CommentDto();
        commentDto.setContents("comment contents!");

        postUser(webTestClient, userDto, postResponseSignup -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                createArticle(webTestClient, articleDto, sessionId, addArticle -> {
                    createComment(webTestClient, commentDto, 1L, sessionId, addComment -> {
                        articleView(webTestClient, sessionId, 1L, result -> {
                            assertThat(new String(result.getResponseBody())).contains("comment contents!");
                        });
                    });
                });
            });
        });
    }

    public static String getSessionId(EntityExchangeResult<byte[]> postUserResponse) {
        return ";jsessionid=" + postUserResponse.getResponseCookies().getFirst("JSESSIONID").getValue();
    }

    public static void postUser(WebTestClient webTestClient, UserDto userDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void loginUser(WebTestClient webTestClient, LoginDto loginDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/login")
                .body(fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void createArticle(WebTestClient webTestClient, ArticleDto articleDto, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/articles" + sessionId)
                .body(BodyInserters.fromFormData("title", articleDto.getTitle())
                        .with("coverUrl", articleDto.getCoverUrl())
                        .with("contents", articleDto.getContents()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void createComment(WebTestClient webTestClient, CommentDto commentDto,Long articleId, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/articles/" + articleId + sessionId)
                .body(BodyInserters.fromFormData("contents", commentDto.getContents()))
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void articleView(WebTestClient webTestClient, String sessionId, Long articleId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/articles/" + articleId + sessionId)
                .exchange()
                .expectBody()
                .consumeWith(consumer);
    }

    @Test
    void writeForm_test() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void save_test() {
        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange()
                .expectStatus()
                .isFound();

        ++id;
    }

    @Test
    void update_test() {
        insertArticle();

        webTestClient.put().uri("/articles/" + id)
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange()
                .expectHeader().valueMatches("location", "(http://localhost:)(.*)(/articles/" + id + ")")
                .expectStatus()
                .isFound();

        deleteArticle();
    }

    @Test
    void delete_test() {
        insertArticle();

        webTestClient.delete().uri("/articles/" + id)
                .exchange()
                .expectStatus()
                .isFound();
    }

    private void deleteArticle() {
        webTestClient.delete().uri("/articles/" + id)
                .exchange();
    }

    private void insertArticle() {
        ++id;

        webTestClient.post().uri("/articles")
                .body(BodyInserters.fromFormData("title", "title")
                        .with("coverUrl", "coverUrl")
                        .with("contents", "contents"))
                .exchange();
    }
}
