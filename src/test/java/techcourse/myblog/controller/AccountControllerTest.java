package techcourse.myblog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.MyblogApplicationTests;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.controller.AccountController.ACCOUNT_URL;


public class AccountControllerTest extends MyblogApplicationTests {

    @Test
    @DisplayName("회원가입 페이지 들어갈때 잘 들어가는 지 확인")
    void showSignupPageTest() {
        getRequestExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/signup").isOk();
    }

    @Test
    @DisplayName("회원가입폼 제출하고 리다이렉트 잘 되는 지 확인")
    void processSignupTest() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("kangmin", "kangmin789@naver.com", "asdASD12!@", 3);
        getResponseSpec(HttpMethod.POST, ACCOUNT_URL + "/user", map)
                .exchange()
                .expectStatus()
                .isFound();
    }

    @Test
    @DisplayName("회원가입 할때 name유효성 위반일 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_Name1Word() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("a", "kangmin789@naver.com", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할때 name 유효성 11글자 위반일 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_Name11Word() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("aaaaaaaaaaa", "kangmin789@naver.com", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    private void judgeSignupValidation(MultiValueMap<String, String> map) {
        getResponseSpec(HttpMethod.POST, ACCOUNT_URL + "/user", map)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("회원가입 할때 name 유효성 숫자가 포함되서 위반일 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NameNotNumber() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcde1a", "kangmin789@naver.com", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할때 name 유효성 특수문자가 포함되서 위반일 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NameNotSpecialCharacter() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcde*a", "kangmin789@naver.com", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할때 이메일 골뱅이 없을 때 유효성체크 후 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_Email() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "abcd.d", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할때 이메일 기본문자열일 때 유효성체크 후 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_Basic_Email() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "abcd", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할때 이메일 골뱅이 앞이 없을 때 유효성체크 후 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NoString_Before_At_Email() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "@asdsa", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할때 이메일 골뱅이 뒤가 없을 때 유효성체크 후 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NoString_After_At_Email() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "abc2@", "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할 때 비밀번호 소문자 없을 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NoSmallLetter_Password() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "kangmin789@naver.com", "ASDASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할 때 비밀번호 대문자 없을 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NoCapitalLetter_Password() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "kangmin789@naver.com", "asdasd12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할 때 비밀번호 숫자 없을 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NoNumberLetter_Password() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "kangmin789@naver.com", "asdASD!@!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할 때 비밀번호 특수문자 없을 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_NoUniqueLetter_Password() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "kangmin789@naver.com", "asdASD1212", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할 때 비밀번호 7자리 없을 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void failSignupTest_7Letter_Password() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", "kangmin789@naver.com", "asAD1!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("회원가입 할 때 이메일 중복일 때 계속 회원가입할 창에 머물게 하는 지 확인")
    void signupTest_이메일_중복_확인() {
        MultiValueMap<String, String> map = getCustomUserDtoMap("abcdea", USER_EMAIL, "asdASD12!@", 3);
        judgeSignupValidation(map);
    }

    @Test
    @DisplayName("마이 페이지 접근 확인")
    void access_Mypage() {
        WebTestClient.ResponseSpec responseSpec = getRequestExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/profile/" + USER_ID)
                .isOk();
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(USER_NAME)).isTrue();
            assertThat(body.contains(USER_EMAIL)).isTrue();
        };
        confirmResponseBody(responseSpec, entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("마이 페이지 수정페이지 접근 확인")
    void access_Mypage_Edit() {
        WebTestClient.ResponseSpec responseSpec = getRequestExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/profile/" + USER_ID)
                .isOk();
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(USER_NAME)).isTrue();
            assertThat(body.contains(USER_EMAIL)).isTrue();
        };
        confirmResponseBody(responseSpec, entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("미 로그인시 수정페이지 접근 리다이렉트")
    void no_Login_Mypage_Exceess() {
        WebTestClient.ResponseSpec responseSpec = getRequestExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/profile/edit")
                .isFound();

        responseSpec.expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseHeaders().getLocation().toString().contains("login")).isTrue();
                });
    }

    @Test
    @DisplayName("로그인후 마이 페이지 수정 후 저장 성공")
    void mypage_Update() {
        String cookie = getLoginCookie(USER_EMAIL, USER_PASSWORD);
        MultiValueMap<String, String> map = getCustomUserDtoMap(USER_NAME + "a", USER_EMAIL, USER_PASSWORD, USER_ID);
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(USER_NAME + "a")).isTrue();
            assertThat(body.contains(USER_EMAIL)).isTrue();
        };

        getResponseSpecWithCookieWithBody(HttpMethod.PUT, ACCOUNT_URL + "/profile/edit", cookie, map)
                .isFound()
                .expectBody()
                .consumeWith(response -> {
                    WebTestClient.ResponseSpec secondResponseSpec = getRequestWithCookieExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/profile/edit", cookie)
                            .isOk();
                    confirmResponseBody(secondResponseSpec, entityExchangeResultConsumer);

                });

        MultiValueMap<String, String> secondMap = getCustomUserDtoMap(USER_NAME, USER_EMAIL, USER_PASSWORD, USER_ID);
        getResponseSpecWithCookieWithBody(HttpMethod.PUT, ACCOUNT_URL + "/profile/edit", cookie, secondMap).isFound();
    }

    @Test
    @DisplayName("유저 리스트 페이지 확인")
    void showUsersPage() {
        WebTestClient.ResponseSpec responseSpec = getRequestExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/users")
                .isOk();
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(USER_NAME)).isTrue();
            assertThat(body.contains(USER_EMAIL)).isTrue();
        };
        confirmResponseBody(responseSpec, entityExchangeResultConsumer);
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser() {
        String cookie = getLoginCookie(USER_EMAIL, USER_PASSWORD);
        getRequestWithCookieExpectStatus(HttpMethod.DELETE, ACCOUNT_URL + "/user", cookie)
                .isFound();
        Consumer<EntityExchangeResult<byte[]>> entityExchangeResultConsumer = (response) -> {
            String body = new String(response.getResponseBody());
            assertThat(body.contains(USER_EMAIL)).isFalse();
        };

        getRequestExpectStatus(HttpMethod.GET, ACCOUNT_URL + "/users")
                .isOk()
                .expectBody()
                .consumeWith(entityExchangeResultConsumer);
    }
}
