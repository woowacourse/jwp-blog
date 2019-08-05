package techcourse.myblog.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.ArticleDataForTest;
import techcourse.myblog.data.UserDataForTest;

import java.net.URI;

@Slf4j
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestTemplate {

    @Autowired
    public WebTestClient webTestClient;

    public WebTestClient.ResponseSpec loggedInGetRequest(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", getCookie())
                .exchange();
    }

    public WebTestClient.ResponseSpec loggedInGetRequest(URI uri) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", getCookie())
                .exchange();
    }

    public WebTestClient.ResponseSpec loggedInGetRequest(String uri, String email, String password) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", getCookie(email, password))
                .exchange();
    }

    public WebTestClient.RequestBodySpec loggedInPostRequest(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", getCookie())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPostRequest(String uri, String email, String password) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", getCookie(email, password))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPutRequest(String uri) {
        return webTestClient
                .put()
                .uri(uri)
                .header("Cookie", getCookie())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPutRequest(String uri, String email, String password) {
        return webTestClient
                .put()
                .uri(uri)
                .header("Cookie", getCookie(email, password))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.ResponseSpec loggedInDeleteRequest(String uri) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie())
                .exchange();
    }

    public WebTestClient.ResponseSpec loggedInDeleteRequest(String uri, String email, String password) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie(email, password))
                .exchange();
    }

    public WebTestClient.ResponseSpec loggedOutGetRequest(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .exchange();
    }

    public WebTestClient.RequestBodySpec loggedOutPostRequest(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPostAjaxRequest(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", getCookie())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    public WebTestClient.RequestBodySpec loggedInPutAjaxRequest(String uri) {
        return webTestClient
                .put()
                .uri(uri)
                .header("Cookie", getCookie())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    public WebTestClient.RequestHeadersSpec<?> loggedInDeleteAjaxRequest(String uri) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie())
                .accept(MediaType.APPLICATION_JSON_UTF8);
    }

    public String createArticle() {
        final String[] path = new String[1];
        loggedInPostRequest("/articles")
                .body(BodyInserters
                        .fromFormData("title", ArticleDataForTest.ARTICLE_TITLE)
                        .with("coverUrl", ArticleDataForTest.ARTICLE_COVER_URL)
                        .with("contents", ArticleDataForTest.ARTICLE_CONTENTS))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(response -> {
                    path[0] = response.getResponseHeaders().getLocation().getPath();
                });

        return path[0];
    }

    private String getCookie() {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    private String getCookie(String email, String password) {
        return webTestClient
                .post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}