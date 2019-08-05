package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.BodyInserters;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserControllerTest extends ControllerTest {
    private static long userId = 1;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void 회원가입_페이지_이동_테스트() {
        webTestClient.get().uri("/signup")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 로그인_후_회원가입_페이지_이동_테스트() {
        webTestClient.get().uri("/signup")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/");
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        String duplicatedEmail = "email@gmail.com";

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", duplicatedEmail)
                .with("password", "password1234!")
                .with("name", "name"))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        String wrongName = "a";

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", "email@gmail.com")
                .with("password", "password")
                .with("name", wrongName))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        String wrongPassword = "password";

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", "email@gmail.com")
                .with("password", wrongPassword)
                .with("name", "name"))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        String wrongEmail = "email";

        webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", wrongEmail)
                .with("password", "password1234!")
                .with("name", "name"))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void 로그인_후_회원가입_요청_테스트() {
        String wrongEmail = "email";

        webTestClient.post().uri("/users")
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("email", wrongEmail)
                .with("password", "password1234!")
                .with("name", "name"))
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/");
    }

    @Test
    void 로그인_후_회원_정보_전체_조회_테스트() {
        webTestClient.get().uri("/users")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 비로그인_회원_정보_전체_조회_테스트() {
        webTestClient.get().uri("/users")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void MyPage_이동_테스트() {
        webTestClient.get().uri("/mypage/" + userId)
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 비로그인_MyPage_이동_성공_테스트() {
        webTestClient.get().uri("/mypage/" + userId)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void EditMyPage_이동_성공_테스트() {
        webTestClient.get().uri("/mypage/" + userId + "/edit")
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void 비로그인_EditMyPage_이동_테스트() {
        webTestClient.get().uri("/mypage/" + userId + "/edit")
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/login");
    }

    @Test
    void 회원_정보_수정_성공_테스트() {
        webTestClient.put().uri("/users/" + userId)
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("name", "newName"))
            .exchange()
            .expectStatus().isFound();
    }

    @Test
    void 비로그인_회원_정보_수정_테스트() {
        webTestClient.put().uri("/users/" + userId)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("name", "newName"))
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/login");
    }

    @Test
    void 회원_정보_수정_실패_테스트() {
        webTestClient.put().uri("/users/" + userId)
            .header("Cookie", cookie)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters
                .fromFormData("name", "A"))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void 비로그인_회원_탈퇴_테스트() {
        webTestClient.delete().uri("/users/" + 1)
            .exchange()
            .expectStatus().isFound().expectHeader().valueMatches("location", "(.)*/login");
    }

    @AfterEach
    void 회원_탈퇴_성공_테스트() {
        webTestClient.delete().uri("/users/" + userId++)
            .header("Cookie", cookie)
            .exchange()
            .expectStatus().isFound();
    }
}
