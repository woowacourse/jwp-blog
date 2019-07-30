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

    private String jSessionId;

    @BeforeEach
    void setUp_회원가입() {
        LoginTestUtil.signUp(webTestClient);
        jSessionId = LoginTestUtil.getJSessionId(webTestClient);
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
        UserSaveRequestDto userSaveRequestDto = LoginTestUtil.getUserSaveRequestDto();

        webTestClient.post().uri("/mypage/edit")
                .body(BodyInserters
                        .fromFormData("password", userSaveRequestDto.getPassword()))
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @AfterEach
    void tearDown_유저_삭제() {
        LoginTestUtil.deleteUser(webTestClient);
    }
}