package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthedWebTestClient {
    private static final Logger log = LoggerFactory.getLogger(AuthedWebTestClient.class);

    @Autowired
    protected WebTestClient webTestClient;

    private String loginCookie() {
        log.debug("Start Authed Session ...");
        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
        log.debug("Authed Session {} ", cookie);
        return cookie;
    }

    protected WebTestClient.RequestBodySpec post(String uri) {
        String cookie = loginCookie();
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestBodySpec postJson(String uri) {
        String cookie = loginCookie();
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8);
    }

    protected WebTestClient.RequestBodySpec put(String uri) {
        String cookie = loginCookie();
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestBodySpec putJson(String uri) {
        String cookie = loginCookie();
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8);
    }

    protected WebTestClient.RequestHeadersSpec get(String uri) {
        String cookie = loginCookie();
        return webTestClient.get().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec delete(String uri) {
        String cookie = loginCookie();
        return webTestClient.delete().uri(uri).header("Cookie", cookie);
    }

    protected BodyInserters.FormInserter<String> params(List<String> keyNames, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData("", "");
        for (int i = 0; i < keyNames.size(); i++) {
            body.with(keyNames.get(i), parameters[i]);
        }
        return body;
    }
}
