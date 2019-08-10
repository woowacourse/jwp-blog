package techcourse.myblog.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import techcourse.myblog.service.dto.UserRequestDto;

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
        UserRequestDto userRequestDto = new UserRequestDto(NAME, PASSWORD, EMAIL);

        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userRequestDto), UserRequestDto.class)
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
        log.debug("cookie : {}", cookie);

        return cookie;
    }

    private String getCookie(String email, String password) {
        UserRequestDto userRequestDto = new UserRequestDto(NAME, password, email);

        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userRequestDto), UserRequestDto.class)
                .exchange()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
        log.debug("cookie : {}", cookie);

        return cookie;
    }
}
