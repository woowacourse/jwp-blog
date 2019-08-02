package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.testutil.LoginTestUtil;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ArticleService articleService;

    private UserSaveRequestDto userSaveRequestDto;
    private String location;
    private String jSessionId;

    @BeforeEach
    void setUp_save_article() {
        userSaveRequestDto = new UserSaveRequestDto("테스트", "article@test.com", "password1!");

        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        jSessionId = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);

        location = webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", "제목")
                        .with("coverUrl", "주소")
                        .with("contents", "내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*")
                .returnResult(String.class)
                .getResponseHeaders()
                .get("Location").get(0);
    }

    @Test
    void writeArticleForm() {
        webTestClient.get().uri("/articles/writing")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fetchArticle() {
        webTestClient.get().uri(location)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void editArticle() {
        webTestClient.get().uri(location + "/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void saveArticle() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.post().uri("/articles")
                .body(BodyInserters
                        .fromFormData("title", "새 제목")
                        .with("coverUrl", "새 url")
                        .with("contents", "새 내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/articles/.*");

        String[] strings = responseSpec.returnResult(String.class)
                .getResponseHeaders()
                .get("Location").get(0)
                .split("/");
        String articleId = strings[strings.length - 1];

        webTestClient.delete().uri("/articles/" + articleId)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void fetchArticle_없는_글_번호로_조회할_경우() {
        long lastId = getLastArticleId();

        webTestClient.get().uri("/articles/" + (++lastId))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void editArticle_없는_글_번호로_수정_요청할_경우() {
        long lastId = getLastArticleId();

        webTestClient.put().uri("/articles/" + (++lastId))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteArticle_없는_글_번호로_삭제_요청할_경우() {
        long lastId = getLastArticleId();

        webTestClient.delete().uri("/articles/" + (++lastId))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    private long getLastArticleId() {
        List<Article> articles = new ArrayList<>();
        for (final Article article : articleService.findAllArticles()) {
            articles.add(article);
        }

        return articles.stream()
                .map(Article::getId)
                .mapToLong(value -> value)
                .max()
                .orElse(0L);
    }

    @Test
    void editArticle_다른_User가_수정_요청할_경우() {
        UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto("테스트", "test1234@test.com", "password1!");
        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        String jSessionIdByAnotherUser = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);

        webTestClient.get().uri(location + "/edit")
                .cookie("JSESSIONID", jSessionIdByAnotherUser)
                .exchange()
                .expectStatus().is3xxRedirection();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }

    @Test
    void saveEditedArticle() {
        webTestClient.put().uri(location)
                .body(BodyInserters
                        .fromFormData("title", "수정된_제목")
                        .with("coverUrl", "수정된_주소")
                        .with("contents", "수정된_내용"))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void deleteArticle_다른_User가_삭제_요청할_경우() {
        UserSaveRequestDto userSaveRequestDto
                = new UserSaveRequestDto("테스트", "deleteArticleByAnotherUser@test.com", "password1!");
        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        String jSessionIdByAnotherUser = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);

        webTestClient.delete().uri(location)
                .cookie("JSESSIONID", jSessionIdByAnotherUser)
                .exchange()
                .expectStatus().isOk();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }

    @AfterEach
    void tearDown_delete_article() {
        webTestClient.delete().uri(location)
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }
}
