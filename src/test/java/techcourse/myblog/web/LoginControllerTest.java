package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.user.User;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest extends AbstractControllerTest{

    @Autowired
    private WebTestClient webTestClient;
    private User user = new User("heejoo", "heejoo@gmail.com", "Aa12345!");

    @Test
    void 로그인_페이지_접근() {
        webTestClient.get().uri("/login").exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_후_로그인_페이지_접근() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.get().uri("/login")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 로그인_성공() {
        getResponseSpec(user.getEmail(), user.getPassword())
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/.*");
    }

    @Test
    void 로그인_실패_이메일_오류() {
        getResponseSpec("CU@gmail.com","password")
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_실패_패스워드_오류() {
        getResponseSpec(user.getEmail(), "password")
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그아웃_페이지_접근() {
        webTestClient.get().uri("/logout")
                .exchange()
                .expectStatus()
                .isFound();
    }

    private WebTestClient.ResponseSpec getResponseSpec(String email, String password) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", email)
                        .with("password", password))
                .exchange();
    }

}
