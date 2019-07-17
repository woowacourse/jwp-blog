package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {
    private WebTestClient webTestClient;
    private String testName = "abcdeFGHIJ";
    private String testPassword = "abcdEFGH123!@#";
    private String testEmail = "abc@hi.com";

    @Autowired
    public AccountControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void showSignupPageTest() {
        webTestClient.get()
                .uri("/accounts/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void processSignupTest() {
        testSignupProcess(testName, testPassword, testEmail)
                .expectStatus()
                .isFound();
    }

    @Test
    void failSignupTest_Name1Word() {
        String wrongName = "a";
        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_Name11Word() {
        String wrongName = "abcdefghijk";
        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_NameNotNumber() {
        String wrongName = "abcde1a";

        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_NameNotSpecialCharacter() {
        String wrongName = "abcde*a";

        testSignupProcess(wrongName, testPassword, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSignupTest_email골뱅이_없음() {
        String wrongEmail = "abcd.d";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
                ;
    }

    @Test
    void failSignupTest_email기본문자열() {
        String wrongEmail = "abcd";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSignupTest_email골뱅이_앞이_없음() {
        String wrongEmail = "@asdsa";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSignupTest_email골뱅이_뒤가_없음() {
        String wrongEmail = "abc2@";

        testSignupProcess(testName, testPassword, wrongEmail)
                .expectStatus()
                .isOk()
        ;
    }

    @Test
    void failSigupTest_Pw소문자없음() {
        String wrongPw = "EFGH123!@#";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw대문자없음() {
        String wrongPw = "abcd123!@#";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw숫자없음() {
        String wrongPw = "abcdEFGH!@#";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw특수문자없음() {
        String wrongPw = "abcdEFGH123";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    @Test
    void failSigupTest_Pw7자리() {
        String wrongPw = "abcFG1!";
        testSignupProcess(testName, wrongPw, testEmail)
                .expectStatus()
                .isOk();
    }

    private WebTestClient.ResponseSpec testSignupProcess(String name, String password, String email) {
        return webTestClient.post()
                .uri("/accounts/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange()
                ;
    }
}
