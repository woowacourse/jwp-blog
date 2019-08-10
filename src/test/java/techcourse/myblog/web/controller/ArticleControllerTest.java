package techcourse.myblog.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.template.LoginTemplate;

public class ArticleControllerTest extends LoginTemplate {
    private static final String SECOND_USER_NAME = "두번째유저";
    private static final String SECOND_USER_EMAIL = "second@user.com";
    private static final String SECOND_USER_PASSWORD = "Second22@";

    private String title = "제목";
    private String contents = "contents";
    private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

    @BeforeEach
    void setUp() {
        registeredWebTestClient();
    }

    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_작성_페이지로_잘_이동하는지_테스트() {
        loggedInGetRequest("/writing")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 게시글_작성_테스트() {
        loggedInPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 등록된_게시글_페이지에_정상적으로_이동하는지_테스트() {
        String articleId = getNewArticleId();

        loggedInGetRequest("/articles/" + articleId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글_수정_테스트() {
        String articleId = getNewArticleId();

        loggedInPutRequest("/articles/" + articleId)
                .body(BodyInserters
                        .fromFormData("title", "updatedTitle")
                        .with("coverUrl", "updatedCoverUrl")
                        .with("contents", "updatedContents"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/articles/" + articleId + ".*");
    }

    @Test
    void 게시글_삭제_테스트() {
        String articleId = getNewArticleId();

        loggedInDeleteRequest("/articles/" + articleId)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    @Test
    void 수정하려는_사용자가_게시글_작성자가_아닌_경우_예외처리() {
        registeredWebTestClient(SECOND_USER_NAME, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        String articleId = getNewArticleId();

        loggedInPutRequest("/articles/" + articleId, SECOND_USER_EMAIL, SECOND_USER_PASSWORD)
                .body(BodyInserters
                        .fromFormData("title", "updatedTitle")
                        .with("coverUrl", "updatedCoverUrl")
                        .with("contents", "updatedContents"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/.*");
    }

    @Test
    void 삭제하려는_사용자가_게시글_작성자가_아닌_경우_예외처리() {
        registeredWebTestClient(SECOND_USER_NAME, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        String articleId = getNewArticleId();

        loggedInDeleteRequest("/articles/" + articleId, SECOND_USER_EMAIL, SECOND_USER_PASSWORD)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", "http://localhost:[0-9]+/");
    }

    private String getNewArticleId() {
        return articlePostRequest()
                .getResponseHeaders()
                .getLocation()
                .getPath().split("/")[2];
    }

    public EntityExchangeResult<byte[]> articlePostRequest() {
        String title = "제목";
        String contents = "본문";
        String coverUrl = "배경";

        return loggedInPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("contents", contents)
                        .with("coverUrl", coverUrl))
                .exchange()
                .expectBody()
                .returnResult();
    }
}