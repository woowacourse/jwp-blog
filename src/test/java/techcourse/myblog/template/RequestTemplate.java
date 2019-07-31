package techcourse.myblog.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.data.UserDataForTest;

@Slf4j
public class RequestTemplate extends SignUpTemplate {
    public WebTestClient.ResponseSpec loggedInGetRequest(String uri) {
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