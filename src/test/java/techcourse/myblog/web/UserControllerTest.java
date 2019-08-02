package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.presentation.UserController.EMAIL_DUPLICATION_ERROR_MSG;
import static techcourse.myblog.presentation.UserController.LOGIN_ERROR_MSG;
import static techcourse.myblog.service.dto.UserRequestDto.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private WebTestClient webTestClient;
    private int testId = 1;
    public static String testName = "abcdeFGHI";
    public static String testPassword = "abcdEFGH123!@#";
    public static String testEmail = "abc@hi.com";

    @Autowired
    public UserControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
        trySignup(testName, testPassword, testEmail);
    }

    @Test
    void 회원가입_페이지_보여주기_테스트() {
        ResponseSpec rs = webTestClient.get()
                .uri("/accounts/signup")
                .exchange();

        rs.expectStatus()
                .isOk();
    }

    @Test
    void 회원가입_성공_테스트() {
        testSignupSuccess("moomin", "123!@#qweQWE", "moomin@naver.com");
    }

    @Test
    void 회원가입_실패_너무_짧은_이름_테스트() {
        testSignupFail("a", testPassword, testEmail, NAME_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_너무_긴_이름_테스트() {
        testSignupFail("abcdefghijk", testPassword, testEmail, NAME_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_숫자가_들어간_이름_테스트() {
        testSignupFail("abcde1a", testPassword, testEmail, NAME_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_특수문자가_들어간_이름_테스트() {
        testSignupFail("abcde*a", testPassword, testEmail, NAME_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_이메일에_골뱅이가_없음_테스트() {
        testSignupFail(testName, testPassword, "abcd.d", EMAIL_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_평범한_문자열_이메일_테스트() {
        testSignupFail(testName, testPassword, "abcd", EMAIL_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_이메일_골뱅이_앞부분이_비어있음_테스트() {
        testSignupFail(testName, testPassword, "@asdsa", EMAIL_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_이메일_골뱅이_뒷부분이_비어있음_테스트() {
        testSignupFail(testName, testPassword, "abc2@", EMAIL_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_비밀번호_소문자가_없음_테스트() {
        testSignupFail(testName, "EFGH123!@#", testEmail, PASSWORD_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_비밀번호_대문자가_없음_테스트() {
        testSignupFail(testName, "abcd123!@#", testEmail, PASSWORD_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_비밀번호_숫자가_없음_테스트() {
        testSignupFail(testName, "abcdEFGH!@#", testEmail, PASSWORD_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_비밀번호_특수문자가_없음_테스트() {
        testSignupFail(testName, "abcdEFGH123", testEmail, PASSWORD_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_너무_짧은_비밀번호_테스트() {
        testSignupFail(testName, "abcFG1!", testEmail, PASSWORD_ERROR_MSG);
    }

    @Test
    void 회원가입_실패_중복된_이메일_테스트() {
        testSignupFail(testName, testPassword, testEmail, EMAIL_DUPLICATION_ERROR_MSG);  // 생성자에서 추가한 테스트 유저와 같은 이메일로 다시 회원가입 시도
    }

    @Test
    void 로그인_성공_테스트() {
        LogInTestHelper.makeLoggedInCookie(webTestClient);
    }

    @Test
    void 로그인_실패_존재하지_않는_이메일_테스트() {
        testLoginFail("pobi@woowahan.com", testPassword);
    }

    @Test
    void 로그인_실패_비밀번호_불일치_테스트() {
        testLoginFail(testEmail, "zxcZXC789&*(");
    }

    @Test
    void 로그아웃_성공_테스트() {
        String cookie = LogInTestHelper.makeLoggedInCookie(webTestClient);

        testLogoutSuccess(cookie);
    }

    @Test
    void 로그아웃_실패_로그인_하지_않고_시도_테스트() {
        testLogoutFail();
    }

    @Test
    void 프로필_페이지_접근_테스트() {
        ResponseSpec rs = webTestClient.get()
                .uri("/accounts/profile/" + testId)
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(testEmail)).isTrue();
                    assertThat(body.contains(testName)).isTrue();
                });
    }

    @Test
    void 프로필_수정_페이지_접근() {
        String cookie = LogInTestHelper.makeLoggedInCookie(webTestClient);

        ResponseSpec rs = webTestClient.get()
                .uri("/accounts/profile/edit")
                .header("Cookie", cookie)
                .exchange();

        rs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(testName)).isTrue();
                    assertThat(body.contains(testEmail)).isTrue();
                });
    }

    @Test
    void 마이페이지_수정_후_저장_성공() {
        String cookie = LogInTestHelper.makeLoggedInCookie(webTestClient);

        ResponseSpec updateRs = webTestClient.put()
                .uri("/accounts/profile/edit")
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", testName + "a")
                        .with("email", testEmail)
                        .with("password", testPassword)
                        .with("id", String.valueOf(testId)))
                .exchange();

        // 잘 변경을 시도했는지를 검증
        updateRs.expectStatus()
                .isFound();

        // 내용이 변경되었는지를 검증
        ResponseSpec readRs = webTestClient.get()
                .uri("/accounts/profile/edit")
                .header("Cookie", cookie)
                .exchange();

        readRs.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(innerResponse -> {
                    String body = new String(innerResponse.getResponseBody());
                    assertThat(body.contains(testName + "a")).isTrue();
                    assertThat(body.contains(testEmail)).isTrue();
                });


        // 원상복구하는 코드... ㅠ...
        // TODO: 각 테스트에 독립적으로 동작할 수 있도록 변경
        webTestClient.put().uri("/accounts/profile/edit").header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", testName)
                        .with("email", testEmail)
                        .with("password", testPassword)
                        .with("id", String.valueOf(testId)))
                .exchange()
                .expectStatus()
                .isFound();
    }

    private ResponseSpec trySignup(String name, String password, String email) {
        return webTestClient.post()
                .uri("/accounts/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange();
    }

    private void testSignupSuccess(String name, String password, String email) {
        ResponseSpec rs = trySignup(name, password, email);

        rs.expectStatus()
                .isFound();  // 회원가입에 성공하면 홈으로 리다이렉션
    }


    private void testSignupFail(String name, String password, String email, String errorMsg) {
        ResponseSpec rs = trySignup(name, password, email);

        rs.expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(errorMsg)).isTrue();
                });
    }

    private ResponseSpec tryLogin(String email, String password) {
        return LogInTestHelper.tryLogin(webTestClient, email, password);
    }

    private void testLoginFail(String email, String password) {
        ResponseSpec rs = tryLogin(email, password);

        rs.expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body.contains(LOGIN_ERROR_MSG)).isTrue();
                });
    }

    private void testLogoutSuccess(String cookie) {
        ResponseSpec rs = webTestClient.get()
                .uri("/logout")
                .header("Cookie", cookie)
                .exchange();

        rs.expectStatus()
                .isFound()
                .expectBody()
                .consumeWith(innerResponse -> {
                    assertThat(innerResponse.getResponseHeaders()
                            .getLocation()
                            .toString()
                            .contains("login")).isFalse();  // 로그아웃에 성공하면 홈으로 간다.
                });
    }

    private void testLogoutFail() {
        ResponseSpec rs = webTestClient.get()
                .uri("/logout")
                .exchange();

        LogInTestHelper.assertLoginRedirect(rs);
    }
}
