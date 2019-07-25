package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest extends AbstractControllerTest{

    @Autowired
    WebTestClient webTestClient;

    @Test
    void 로그인_페이지_접근() {
        webTestClient.get().uri("/login").exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_후_로그인_페이지_접근() {
        String jSessionId = getJSessionId("Buddy","buddy@gmail.com","Aa12345!");
        webTestClient.get().uri("/login")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_성공() {
        String email = "buddy@gmail.com";
        String password = "Aa12345!";
        create_user("Buddy",email,password);

        getResponseSpec(email,password)
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
        create_user("ssosso","ssosso@gamil.com","Aa12345!");

        getResponseSpec("ssosso@gmail.com","password")
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
