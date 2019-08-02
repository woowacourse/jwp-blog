package techcourse.myblog.interceptor;

import org.junit.jupiter.api.Test;
import techcourse.myblog.web.AuthedWebTestClient;

class LoginInterceptorTest extends AuthedWebTestClient {


    @Test
    void 인터셉터_동작() {
        webTestClient.get().uri("/writing")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+/login");
    }

    @Test
    void 인터셉터_동작2() {
        webTestClient.get().uri("/articles/1")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".+/login");
    }


    @Test
    void 인터셉터_동작_제외_URL_index() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 인터셉터_동작_제외2_URL_signup() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 인터셉터_동작_제외3_URL_login() {
        webTestClient.get().uri("/login")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 인터셉터_우회_URL_users() {
        get("/users")
                .exchange()
                .expectStatus().isOk();
    }
}