package techcourse.myblog.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.CommentDto;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests extends AuthedWebTestClient {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String SEAN_NAME = "sean";
    private static final String SEAN_EMAIL = "sean@gmail.com";
    private static final String POBI_NAME = "pobi";
    private static final String POBI_EMAIL = "pobi@gmail.com";
    private static final String DEFAULT_PASSWORD = "Woowahan123!";
    private static final String CONTENT = "contents";
    private static final String DEFAULT_URL = "/";
    private static final String UPDATED_COMMENT = "updated_comment";

    private static int SEAN_ARTICLE_ID;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    private int commentId = 0;

    @BeforeEach
    void 게시글_댓글_작성() {
        userRepository.save(new User(SEAN_NAME, SEAN_EMAIL, DEFAULT_PASSWORD));
        userRepository.save(new User(POBI_NAME, POBI_EMAIL, DEFAULT_PASSWORD));

        post(SEAN_EMAIL)
                .expectBody()
                .consumeWith(response -> {
                    String path = Objects.requireNonNull(response.getResponseHeaders().getLocation()).getPath();
                    int index = path.lastIndexOf(DEFAULT_URL);
                    SEAN_ARTICLE_ID = Integer.parseInt(path.substring(index + 1));
                });

        commentId++;
        addComments();
    }

    @Test
    void 모든_댓글_조회() {
        addComments();

        webTestClient.get().uri("/articles/" + SEAN_ARTICLE_ID + "/comments")
                .cookie(JSESSIONID, getResponseCookie(SEAN_EMAIL, DEFAULT_PASSWORD).getValue())
                .exchange()
                .expectBody()
                .jsonPath("$.comments.length()").isEqualTo(2);
    }

    @Test
    void 댓글_생성() {
        CommentDto commentDto = new CommentDto(CONTENT);

        webTestClient.post().uri("/articles/" + SEAN_ARTICLE_ID + "/comments")
                .cookie(JSESSIONID, getResponseCookie(POBI_EMAIL, DEFAULT_PASSWORD).getValue())
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(2L)
                .jsonPath("$.contents").isEqualTo(CONTENT)
                .jsonPath("$.author.name").isEqualTo(POBI_NAME);
    }

    @Test
    void 댓글_수정() {
        getStatus(POBI_EMAIL, UPDATED_COMMENT)
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.contents").isEqualTo(UPDATED_COMMENT);
    }

    @Test
    void 다른_사람이_댓글_삭제() {
        String deletePath = "/articles/" + SEAN_ARTICLE_ID + "/comments/" + commentId;

        delete(deletePath, SEAN_EMAIL, DEFAULT_PASSWORD)
                .is3xxRedirection();
    }

    @Test
    void 다른_사람이_댓글_수정() {
        getStatus(SEAN_EMAIL, UPDATED_COMMENT)
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 댓글_삭제() {
        String deletePath = "/articles/" + SEAN_ARTICLE_ID + "/comments/" + commentId;

        delete(deletePath, POBI_EMAIL, DEFAULT_PASSWORD)
                .isOk();
    }

    @AfterEach
    void setUp() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void addComments() {
        CommentDto commentDto = new CommentDto(CONTENT);

        webTestClient.post().uri("/articles/" + SEAN_ARTICLE_ID + "/comments")
                .cookie(JSESSIONID, getResponseCookie(POBI_EMAIL, DEFAULT_PASSWORD).getValue())
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange();
    }

    private WebTestClient.ResponseSpec getStatus(String pobiEmail, String comment) {
        CommentDto commentDto = new CommentDto(UPDATED_COMMENT);

        return webTestClient.put().uri("/articles/" + SEAN_ARTICLE_ID + "/comments/" + commentId)
                .cookie(JSESSIONID, getResponseCookie(pobiEmail, DEFAULT_PASSWORD).getValue())
                .body(Mono.just(commentDto), CommentDto.class)
                .exchange();
    }
}
