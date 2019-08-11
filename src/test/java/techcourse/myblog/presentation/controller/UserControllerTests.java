package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.application.dto.UserRequestDto;
import techcourse.myblog.presentation.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static techcourse.myblog.utils.UserTestObjects.READER_DTO;
import static techcourse.myblog.utils.UserTestObjects.SIGN_UP_USER_DTO;

class UserControllerTests extends ControllerTestTemplate {
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

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    void 회원가입_유효성_에러_테스트(String name, String email, String password) {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, "/users", parseUser(new UserRequestDto(name, email, password))));

        assertEquals("/signup", redirectUrl);
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("wrong!name", "e@mail.com", "p@ssw0RD!"),
                Arguments.of("name", "wrong", "p@ssw00RD!"),
                Arguments.of("name", "e@mail.com", "잘못된패스워드"),
                Arguments.of("name", SIGN_UP_USER_DTO.getEmail(), "passw0RD!")
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
        httpRequestWithData(POST, "/login", parseUser(savedUserRequestDto)).isFound();
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidLoginParameters")
    void 로그인_유효성_에러_테스트(String name, String email, String password) {
        String redirectUrl = getRedirectUrl(httpRequestWithData(POST, "/login", parseUser(new UserRequestDto(name, email, password))));

        assertEquals("/login", redirectUrl);
    }

    private static Stream<Arguments> invalidLoginParameters() {
        return Stream.of(
                Arguments.of(null, "e@mail.com", "p@sswsavedPassw0RD!"),
                Arguments.of(null, "saved@email.com", "edPassw0RD!")
        );
    }

    @Test
    void 로그아웃_요청() {
        loginAndRequestWriter(GET, "/logout").isFound();
    }
}