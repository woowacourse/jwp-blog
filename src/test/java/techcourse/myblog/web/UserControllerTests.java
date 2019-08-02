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
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.testutil.LoginTestUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private UserSaveRequestDto userSaveRequestDto;
    private String jSessionId;

    @BeforeEach
    void setUp_user_save() {
        userSaveRequestDto = new UserSaveRequestDto("테스트", "user@test.com", "password1!");

        LoginTestUtil.signUp(webTestClient, userSaveRequestDto);
        jSessionId = LoginTestUtil.getJSessionId(webTestClient, userSaveRequestDto);
    }

    @Test
    void saveUser() {
        UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto("newUserName", "newUserEmail@test.com", "password1!");

        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", userSaveRequestDto.getName())
                        .with("email", userSaveRequestDto.getEmail())
                        .with("password", userSaveRequestDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");

        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
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
        webTestClient.post().uri("/mypage/edit")
                .body(BodyInserters
                        .fromFormData("password", userSaveRequestDto.getPassword()))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @AfterEach
    void tearDown_user_delete() {
        LoginTestUtil.deleteUser(webTestClient, userSaveRequestDto);
    }
}