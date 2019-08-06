package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.domain.user.UserTest.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends AbstractControllerTest {

    private static final String email = user.getEmail();
    private static final String userName = user.getUserName();
    private static final String password = user.getPassword();

    public WebTestClient.ResponseSpec getRequest(String uri) {
        return webTestClient.get().uri(uri).exchange();
    }

    @Test
    void 회원가입_페이지() {
        getRequest("/users/new")
                .expectStatus().isOk();
    }

    @Test
    void 유저_생성() {
        getResponseSpec(userName, "brown@gmail.com", password, password)
                .expectStatus().isFound();
    }

    @Test
    void 중복_이메일_확인() {
        WebTestClient.ResponseSpec responseSpec = getResponseSpec(userName, email, password, password)
                .expectStatus().isBadRequest();

        checkInvalidUserMessage(responseSpec, "중복된 이메일 입니다.");
    }

    @Test
    void 이메일_형식_확인() {
        WebTestClient.ResponseSpec responseSpec = getResponseSpec(userName, "buddy", password, password)
                .expectStatus().isBadRequest();

        checkInvalidUserMessage(responseSpec, "형식에 맞는 이메일이 아닙니다.");
    }

    @Test
    void 유저_이름_확인() {
        WebTestClient.ResponseSpec responseSpec = getResponseSpec("J", email, password, password)
                .expectStatus().isBadRequest();
        checkInvalidUserMessage(responseSpec, "형식에 맞는 이름이 아닙니다.");
    }

    @Test
    void 유저_패스워드_확인() {
        WebTestClient.ResponseSpec responseSpec = getResponseSpec(userName, email, "A", "A")
                .expectStatus().isBadRequest();
        checkInvalidUserMessage(responseSpec, "형식에 맞는 비밀번호가 아닙니다.");
    }

    @Test
    void 유저_패스워드_컨펌패스워드_매치_확인() {
        WebTestClient.ResponseSpec responseSpec = getResponseSpec(userName, email, password, "Ss12345!")
                .expectStatus().isBadRequest();

        checkInvalidUserMessage(responseSpec, "비밀번호가 일치하지 않습니다.");
    }

    @Test
    void 로그인_전_유저_리스트_확인() {
        getRequest("/users")
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_유저_리스트_확인() {
        String jSessionId = extractJSessionId(login(user));

        webTestClient.get().uri("/users")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains("heejoo")).isTrue();
            assertThat(body.contains("heejoo@gmail.com")).isTrue();
        });
    }

    @Test
    void 로그인_전_마이페이지_접근() {
        getRequest("/users/mypage")
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_마이페이지_접근() {
        String jSessionId = extractJSessionId(login(user));

        webTestClient.get().uri("/users/mypage")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_전_회원수정페이지_접근() {
        getRequest("/users/mypage/edit")
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("location", ".*/login.*");
    }

    @Test
    void 로그인_후_회원수정페이지_접근() {
        String jSessionId = extractJSessionId(login(user));
        webTestClient.get().uri("/users/mypage/edit")
                .cookie("JSESSIONID", jSessionId)
                .exchange()
                .expectStatus().isOk();
    }

    private void checkInvalidUserMessage(WebTestClient.ResponseSpec responseSpec, String message) {
        responseSpec.expectBody().consumeWith(res -> {
            String body = new String(res.getResponseBody());
            assertThat(body.contains(message)).isTrue();
        });
    }

    private WebTestClient.ResponseSpec getResponseSpec(String userName, String email, String password, String confirmPassword) {
        return webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", confirmPassword)
                ).exchange();
    }

}
