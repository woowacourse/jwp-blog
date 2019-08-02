package techcourse.myblog.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.validation.pattern.UserPattern;
import techcourse.myblog.web.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;

class UserControllerTests extends ControllerTestTemplate {
    private static final String LOGIN_FAIL_MESSAGE = "이메일이나 비밀번호가 올바르지 않습니다";
    private static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";
    
    @Test
    void 로그아웃상태_회원가입_페이지_요청() {
        httpRequest(GET, "/signup").isOk();
    }

    @Test
    void 로그인_상태에서_회원가입_요청시_리다이렉트() {
        loginAndRequestWriter(GET, "/signup").isFound();
    }

    @Test
    void 회원가입_성공시_리다이렉트() {
        httpRequestWithData(POST, "/users", parseUser(READER_DTO)).isFound();
    }

    //todo : 테스트 깨짐
    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    void 회원가입_유효성_에러_테스트(String name, String email, String password, String errorMsg) {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, "/users", parseUser(new UserDto(name, email, password))));
        String responseBody = getResponseBody(httpRequest(GET, redirectUrl));
        
       assertThat(responseBody).contains(errorMsg);
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("wrong!name", "e@mail.com", "p@ssw0RD!", UserPattern.NAME_CONSTRAINT_MESSAGE),
                Arguments.of("name", "wrong", "p@ssw00RD!", UserPattern.EMAIL_CONSTRAINT_MESSAGE),
                Arguments.of("name", "e@mail.com", "잘못된패스워드", UserPattern.PASSWORD_CONSTRAINT_MESSAGE),
                Arguments.of("name", "saved@email.com", "passw0RD!", DUPLICATED_USER_MESSAGE)
        );
    }

    @Test
    void 회원목록_페이지_요청() {
        httpRequest(GET, "/users").isOk();
    }

    @Test
    void 로그아웃상태_로그인_페이지_요청() {
        httpRequest(GET, "/login").isOk();
    }

    @Test
    void 로그인_상태에서_로그인_페이지_요청시_리다이렉트() {
        loginAndRequestWriter(GET, "/login").isFound();
    }

    @Test
    void 로그인_성공_시_메인_화면으로_리다이렉트() {
        httpRequestWithData(POST, "/login", parseUser(savedUserDto)).isFound();
    }

    //todo : 테스트 깨짐
    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidLoginParameters")
    void 로그인_유효성_에러_테스트(String name, String email, String password, String errorMsg) {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, "/login", parseUser(new UserDto(name, email, password))));
        String responseBody = getResponseBody(httpRequest(GET, redirectUrl));

        assertThat(responseBody).contains(errorMsg);
    }

    private static Stream<Arguments> invalidLoginParameters() {
        return Stream.of(
                Arguments.of(null, "e@mail.com", "p@sswsavedPassw0RD!", LOGIN_FAIL_MESSAGE),
                Arguments.of(null, "saved@email.com", "edPassw0RD!", LOGIN_FAIL_MESSAGE)
        );
    }

    @Test
    void 로그아웃_요청() {
        loginAndRequestWriter(GET, "/logout").isFound();
    }
}