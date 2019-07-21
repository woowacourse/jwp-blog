package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.repository.UserRepository;

public class LoginTemplate extends SignUpTemplate {
    private static final Logger log = LoggerFactory.getLogger(LoginTemplate.class);
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;

    public WebTestClient.RequestHeadersSpec loggedInGetRequest(String uri) {
        return webTestClient
                .get()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    public WebTestClient.RequestBodySpec loggedInPostRequest(String uri) {
        return webTestClient
                .post()
                .uri(uri)
                .header("Cookie", getCookie())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestBodySpec loggedInPutRequest(String uri) {
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", getCookie())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public WebTestClient.RequestHeadersSpec loggedInDeleteRequest(String uri) {
        return webTestClient
                .delete()
                .uri(uri)
                .header("Cookie", getCookie());
    }

    private String getCookie() {
        webTestClient = getRegisteredWebTestClient();
        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "andole@gmail.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
        log.debug("cookie : {}", cookie);
        return cookie;
    }
}
