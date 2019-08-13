package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserControllerTest extends LoggedInTemplate {

    private static final String SIGNUP_EMAIL = "ike1@gamil.com";

    private static final String UNREGISTERED_EMAIL = "ike2@gmail.com";
    private static final String UNREGISTERED_PASSWORD = "B!1bcdefg";

    private static final String NAME_TO_UPDATE = "ikee";
    private static final String EMAIL_TO_UPDATE = "ike3@gmail.com";

    private static final String NAME_TO_DELETE = "bye";
    private static final String PASSWORD_TO_DELETE = "ByeBye1!";
    private static final String EMAIL_TO_DELETE = "bye@gmail.com";

    @BeforeEach
    void setUp() {
        signUpUser();
    }

    @Test
    void 유저_리스트_조회_테스트() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus()
                .is3xxRedirection();

    }

    @Test
    void 회원가입_성공_테스트() {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", NAME)
                        .with("password", PASSWORD)
                        .with("email", SIGNUP_EMAIL))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 회원가입_실패_테스트() {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "a")
                        .with("password", "b")
                        .with("email", "c"))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void 로그인_성공_테스트() {
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void 로그인_실패_이메일_없음_테스트() {
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", UNREGISTERED_EMAIL)
                        .with("password", PASSWORD))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("등록된 이메일이 없습니다.")).isTrue();
                });
    }

    @Test
    void 로그인_실패_비밀번호_틀림_테스트() {
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", EMAIL)
                        .with("password", UNREGISTERED_PASSWORD))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains("비밀번호가 일치하지 않습니다.")).isTrue();
                });
    }

    @Test
    void 회원정보_수정_성공_테스트() {
        loggedInPutRequest("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", NAME_TO_UPDATE)
                        .with("email", EMAIL_TO_UPDATE))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/mypage");
    }

    @Test
    void 회원정보_수정_실패_이메일_중복_테스트() {
        signUpUser();
        loggedInPutRequest("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", NAME_TO_UPDATE)
                        .with("email", EMAIL))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/mypage");
    }

    @Test
    void 회원정보_삭제_테스트() {
        signUpUser(NAME_TO_DELETE, PASSWORD_TO_DELETE, EMAIL_TO_DELETE);
        loggedInDeleteRequest("/users", EMAIL_TO_DELETE, PASSWORD_TO_DELETE)
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches("location", ".+/");
    }
}