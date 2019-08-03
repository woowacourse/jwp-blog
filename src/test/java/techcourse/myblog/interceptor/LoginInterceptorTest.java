package techcourse.myblog.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.UserSession;

import static techcourse.myblog.web.UserSession.USER_SESSION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginInterceptorTest {

    @Autowired
    private WebTestClient webTestClient;

    private void createSession() {
        User user = User.builder()
                .email("email@gmail.com")
                .password("P@ssw0rd")
                .name("name")
                .build();

        UserSession userSession = UserSession.createByUser(user);

        webTestClient = WebTestClient.bindToWebHandler(exchange -> exchange.getSession()
                .doOnNext(webSession ->
                        webSession.getAttributes().put(USER_SESSION, userSession))
                .then()).build();
    }

    @Test
    void 로그인후_LoginForm_접근() {
        createSession();

        webTestClient.get().uri("/users/login")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", "/");
    }

    @Test
    void 로그인후_Login_접근() {
        createSession();

        webTestClient.post().uri("/users/login")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", "/");
    }

    @Test
    void 로그인후_SignupForm__접근() {
        createSession();

        webTestClient.get().uri("/users/new")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", "/");
    }
}