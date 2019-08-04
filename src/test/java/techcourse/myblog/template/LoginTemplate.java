package techcourse.myblog.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class LoginTemplate extends SignUpTemplate {
    private static final Logger log = LoggerFactory.getLogger(LoginTemplate.class);

    public WebTestClient.RequestHeadersSpec loggedInGetRequest(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    public WebTestClient.RequestHeadersSpec loggedInGetRequest(String uri, String email, String password) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", getCookie(email, password));
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

    public WebTestClient.RequestHeadersSpec loggedInDeleteRequest(String uri) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    public WebTestClient.RequestHeadersSpec loggedInDeleteRequest(String uri, String email, String password) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie(email, password));
    }

    private String getCookie() {
        String cookie = webTestClient
                .post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
        log.debug("cookie : {}", cookie);
        return cookie;
    }

    private String getCookie(String email, String password) {
        String cookie = webTestClient
                .post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
        log.debug("cookie : {}", cookie);
        return cookie;
    }
}
