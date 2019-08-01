package techcourse.myblog.web;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.CommentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.web.ArticleController.ARTICLE_DEFAULT_URL;
import static techcourse.myblog.web.AuthControllerTest.getJSessionId;
import static techcourse.myblog.web.UserControllerTest.회원_등록;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {

    private static final String TEST_LOGIN_ID = "pkch@woowa.com";
    private static final String TEST_LOGIN_PASSWORD = "!234Qwer";

    private List<String> articleParams = Arrays.asList("title", "coverUrl", "contents");
    private String title = "제목";
    private String coverUrl = "https://naver.com";
    private String contents = "내용";
    private String jSessionId;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;

    private BodyInserters.FormInserter<String> mapBy(String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);

        for (int i = 0; i < parameters.length; i++) {
            body.with(articleParams.get(i), parameters[i]);
        }
        return body;
    }

    @BeforeEach
    void setUp() {
        UserDto testUserDto = new UserDto();
        testUserDto.setName("pkch");
        testUserDto.setEmail(TEST_LOGIN_ID);
        testUserDto.setPassword(TEST_LOGIN_PASSWORD);
        회원_등록(webTestClient, testUserDto);
        jSessionId = getJSessionId(webTestClient, TEST_LOGIN_ID, TEST_LOGIN_PASSWORD);
    }

    @Test
    void indexTest() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void showWritingPageTest() {
        webTestClient.get().uri("/writing")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void articleCreateReadTest() {
        webTestClient.post().uri(ARTICLE_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .body(mapBy(title, coverUrl, contents))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(res ->
                        webTestClient.get().uri(res.getResponseHeaders().getLocation())
                                .exchange()
                                .expectBody()
                                .consumeWith(_res -> {
                                    String body = new String(_res.getResponseBody());
                                    assertTrue(body.contains(title));
                                    assertTrue(body.contains(coverUrl));
                                    assertTrue(body.contains(contents));
                                })
                );
    }

    @Test
    void articleUpdateTest() {
        String updateTitle = "제목2";
        String updateImage = "이미지2";
        String updateContents = "내용2";

        webTestClient.post().uri(ARTICLE_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .body(mapBy(title, coverUrl, contents))
                .exchange()
                .expectBody()
                .consumeWith(res ->
                        webTestClient.put().uri(res.getResponseHeaders().getLocation())
                                .header("cookie", jSessionId)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(mapBy(updateTitle, updateImage, updateContents))
                                .exchange()
                                .expectStatus().is3xxRedirection()
                                .expectBody()
                                .consumeWith(_res ->
                                        webTestClient.get().uri(res.getResponseHeaders().getLocation())
                                                .exchange()
                                                .expectBody()
                                                .consumeWith(__res -> {
                                                    String body = new String(__res.getResponseBody());
                                                    assertTrue(body.contains(title));
                                                    assertTrue(body.contains(coverUrl));
                                                    assertTrue(body.contains(contents));
                                                }))
                );
    }

    @Test
    void articleDelete() {
        webTestClient.post().uri(ARTICLE_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .body(
                        BodyInserters.fromFormData("title", title)
                                .with("coverUrl", coverUrl)
                                .with("contents", contents)
                )
                .exchange()
                .expectBody()
                .consumeWith(res -> webTestClient.delete().uri(res.getResponseHeaders().getLocation())
                        .header("cookie", jSessionId)
                        .exchange()
                        .expectStatus().is3xxRedirection());
    }

    @Test
    void commentCreateTest() {
        given(commentService.create(1L, null, new Comment())).willReturn(new Comment());

        webTestClient.post().uri(ARTICLE_DEFAULT_URL + "/1/comments")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void commentUpdateTest() {
        webTestClient.put().uri(ARTICLE_DEFAULT_URL + "/1/comments/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/articles/1");
    }

    @Test
    void commentDeleteTest() {
        webTestClient.delete().uri(ARTICLE_DEFAULT_URL + "/1/comments/1")
                .header("cookie", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/articles/1");
    }
}