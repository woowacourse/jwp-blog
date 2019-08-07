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
import techcourse.myblog.data.UserDataForTest;

import java.net.URI;

@Slf4j
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestTemplate {

    @Autowired
    public WebTestClient webTestClient;

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

    public WebTestClient.RequestBodySpec loggedInPostRequest(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", getCookie())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPostJsonRequest(String uri) {
        return loggedInPostRequestBody(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    private WebTestClient.RequestBodySpec loggedInPostRequestBody(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    public WebTestClient.RequestBodySpec loggedInPutRequest(String uri) {
        return loggedInPutRequestBody(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPutJsonRequest(String uri) {
        return loggedInPutRequestBody(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    private WebTestClient.RequestBodySpec loggedInPutRequestBody(String uri) {
        return webTestClient
                .put()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    public WebTestClient.ResponseSpec loggedInDeleteRequest(String uri, String email, String password) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie(email, password))
                .exchange();
    }

    public WebTestClient.ResponseSpec loggedInDeleteRequest(String uri) {
        return loggedInDelete(uri)
                .exchange();
    }

    public WebTestClient.RequestHeadersSpec<?> loggedInDeleteJsonRequest(String uri) {
        return loggedInDelete(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8);
    }

    private WebTestClient.RequestHeadersSpec<?> loggedInDelete(String uri) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    private String getCookie() {
        return loggedOutPostRequest("/login")
                .body(BodyInserters
                        .fromFormData("email", UserDataForTest.USER_EMAIL)
                        .with("password", UserDataForTest.USER_PASSWORD))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }

    private String getCookie(String email, String password) {
        return loggedOutPostRequest("/login")
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}