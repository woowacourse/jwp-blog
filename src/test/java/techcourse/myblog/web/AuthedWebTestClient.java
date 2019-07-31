package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthedWebTestClient {
    private static final Logger log = LoggerFactory.getLogger(AuthedWebTestClient.class);
    public User user = new User("andole", "A!1bcdefg", "andole@gmail.com");
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;

    @Rollback(value = false)
    protected void init() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("name", "andole")
                        .with("password", "A!1bcdefg")
                        .with("email", "andole@gmail.com"))
                .exchange();
    }

    protected void end() {
        userRepository.deleteAll();
    }

    private String loginCookie() {
        //TODO 왜 아래처럼 되어 있으면 오류가 날까?
//        long count = userRepository.count();
//        User newUser = userRepository.findByEmail(Email.of(user.getEmail())).get();
        log.debug("Start Authed Session ...");
        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "andole@gmail.com")
                        .with("password", "A!1bcdefg"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
        log.debug("Authed Session {} ", cookie);
        return cookie;
    }

    protected WebTestClient.RequestBodySpec post(String uri) {
        return webTestClient.post().uri(uri).header("Cookie", loginCookie());
    }

    protected WebTestClient.RequestHeadersSpec get(String uri) {
        return webTestClient.get().uri(uri).header("Cookie", loginCookie());
    }

    protected WebTestClient.RequestBodySpec put(String uri) {
        return webTestClient.put().uri(uri).header("Cookie", loginCookie());
    }

    protected WebTestClient.RequestHeadersSpec delete(String uri) {
        return webTestClient.delete().uri(uri).header("Cookie", loginCookie());
    }
}
