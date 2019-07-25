package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserSaveParams;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private String jSessionId;
    private UserSaveParams signUpParams = new UserSaveParams("이름", "test2@test.com", "password2@");

    @BeforeEach
    void setUp() {
        LoginTestConfig.signUp(webTestClient);
        jSessionId = LoginTestConfig.getJSessionId(webTestClient);
    }

    @Test
    void 회원가입_성공() {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", signUpParams.getName())
                        .with("email", signUpParams.getEmail())
                        .with("password", signUpParams.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    void 회원목록_페이지_이동() {
        webTestClient.get().uri("/users")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_페이지_이동() {
        webTestClient.get().uri("/mypage")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_confirm_페이지_이동() {
        webTestClient.get().uri("/mypage/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_edit_페이지_이동() {
        UserSaveParams userSaveParams = LoginTestConfig.getUserSaveParams();

        webTestClient.post().uri("/mypage/edit")
                .body(BodyInserters
                        .fromFormData("password", userSaveParams.getPassword()))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void mypage_delete() {
        LoginTestConfig.signUp(webTestClient, signUpParams);
        String jSessionIdByDelete = LoginTestConfig.getJSessionId(webTestClient, signUpParams);

        webTestClient.delete().uri("/mypage")
                .cookie("JSESSIONID", jSessionIdByDelete)
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @AfterEach
    void tearDown() {
        LoginTestConfig.deleteUser(webTestClient);
    }
}