package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthedWebTestClient {
    private static final String JSESSIONID = "JSESSIONID";
    private static final String URI_ARTICLES = "/articles";
    private static final String DEFAULT_PASSWORD = "Woowahan123!";
    private static final String TITLE = "title";
    private static final String CONTENT = "contents";
    private static final String COVER_URL = "coverUrl";
    private static final String UPDATED_TITLE = "updated_title";
    private static final String UPDATED_COVER_URL = "updated_coverUrl";
    private static final String UPDATED_CONTENT = "updated_content";
    private static final String LOGIN_UTL = "/login";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Autowired
    private WebTestClient webTestClient;

    protected ResponseCookie getResponseCookie(String email, String password) {
        return webTestClient.post().uri(LOGIN_UTL)
                .body(fromFormData(EMAIL, email)
                        .with(PASSWORD, password))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .returnResult(ResponseCookie.class)
                .getResponseCookies()
                .getFirst(JSESSIONID);
    }

    protected StatusAssertions get(String uri, String email, String password) {
        return webTestClient.get().uri(uri)
                .cookie(JSESSIONID, getResponseCookie(email, password).getValue())
                .exchange()
                .expectStatus();
    }

    protected WebTestClient.ResponseSpec post(String email) {
        return webTestClient.post().uri(URI_ARTICLES)
                .cookie(JSESSIONID, getResponseCookie(email, DEFAULT_PASSWORD).getValue())
                .body(fromFormData(TITLE, TITLE)
                        .with(COVER_URL, COVER_URL)
                        .with(CONTENT, CONTENT))
                .exchange()
                .expectStatus()
                .isFound();
    }

    protected WebTestClient.ResponseSpec put(String email, String uri) {
        return webTestClient.put().uri(uri)
                .cookie(JSESSIONID, getResponseCookie(email, DEFAULT_PASSWORD).getValue())
                .body(fromFormData(TITLE, UPDATED_TITLE)
                        .with(COVER_URL, UPDATED_COVER_URL)
                        .with(CONTENT, UPDATED_CONTENT))
                .exchange();
    }

    protected StatusAssertions delete(String uri, String email, String password) {
        return (StatusAssertions) webTestClient.delete().uri(uri)
                .cookie(JSESSIONID, getResponseCookie(email, password).getValue())
                .exchange()
                .expectStatus();
    }
}