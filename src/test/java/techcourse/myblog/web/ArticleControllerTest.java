package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        // 회원가입
        webTestClient.post().uri("/users")
                .body(fromFormData("name", "Bob")
                        .with("email", "test@gmail.com")
                        .with("password", "PassWord1!")
                        .with("reconfirmPassword", "PassWord1!"))
                .exchange()
                .expectStatus()
                .isFound();

        cookie = getCookie("test@gmail.com");

        // 글쓰기
        String title = "titleTest";
        String coverUrl = "coverUrlTest";
        String contents = "contentsTest";
        webTestClient.post()
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", title)
                        .with("coverUrl", coverUrl)
                        .with("contents", contents))
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isFound();

    }

    // TODO: indexControllerTests 만들기
    // (page 관련 테스트)
    @Test
    void index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 글쓰기_접근() {
        webTestClient.get().uri("/articles/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글조회() {
        webTestClient.get()
                .uri("/articles/" + "1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 존재하지_않는_게시글_조회_에러처리() {
        webTestClient.get()
                .uri("/articles/" + "2")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 게시글삭제() {
        String uriForSpecificArticle = "/articles/1";

        webTestClient.delete().uri(uriForSpecificArticle)
                .header("Cookie", cookie)
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/")
                .expectStatus()
                .is3xxRedirection();


        String apiUri = "/api/articles/1";
        readFrom(apiUri).expectStatus()
                .isNoContent();
    }

    @Test
    void 다른사용자가_게시글수정페이지접근_시도_게시글페이지로_이동() {
        webTestClient.get().uri("/logout")
                .exchange();

        // 다른 사람 회원가입
        webTestClient.post().uri("/users")
                .body(fromFormData("name", "Bob")
                        .with("email", "test2@gmail.com")
                        .with("password", "PassWord1!")
                        .with("reconfirmPassword", "PassWord1!"))
                .exchange()
                .expectStatus()
                .isFound();

        String cookie = getCookie("test2@gmail.com");

        webTestClient.get().uri("/articles/1/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/")
                .expectStatus()
                .isFound();
    }

    @Test
    void 다른사용자가_게시글수정_시도_게시글페이지로_이동() {
        webTestClient.get().uri("/logout")
                .exchange();

        // 다른 사람 회원가입
        webTestClient.post().uri("/users")
                .body(fromFormData("name", "Bob")
                        .with("email", "test2@gmail.com")
                        .with("password", "PassWord1!")
                        .with("reconfirmPassword", "PassWord1!"))
                .exchange()
                .expectStatus()
                .isFound();

        String cookie = getCookie("test2@gmail.com");

        webTestClient.put().uri("/articles/1")
                .header("Cookie", cookie)
                .exchange()
                .expectHeader()
                .valueMatches("location", ".*/")
                .expectStatus()
                .isFound();
    }

    @Test
    void 내가쓴글_수정페이지_접근() {
        webTestClient.get().uri("/articles/1/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 내가쓴글_수정_시도() {
        webTestClient.put().uri("/articles/1")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "updated title")
                        .with("coverUrl", "updated coverUrl")
                        .with("contents", "updated contents"))
                .exchange()
                .expectStatus()
                .isFound();
    }

    private ResponseSpec readFrom(String uri) {
        return webTestClient.get()
                .uri(uri)
                .exchange();
    }

    private String getCookie(String email) {
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", email)
                        .with("password", "PassWord1!"))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}
