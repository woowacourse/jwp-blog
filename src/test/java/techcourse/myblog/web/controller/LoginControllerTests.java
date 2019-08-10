package techcourse.myblog.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.web.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static techcourse.myblog.service.exception.LoginFailedException.LOGIN_FAIL_MESSAGE;

class LoginControllerTests extends ControllerTestTemplate {
    @Test
    void 로그아웃상태_로그인_페이지_요청() {
        httpRequest(GET, "/login").isOk();
    }

    @Test
    void 로그인_상태에서_로그인_페이지_요청시_리다이렉트() {
        loginAndRequest(savedUserDto, GET, "/login").isFound();
    }

    @Test
    void 로그인_성공_시_메인_화면으로_리다이렉트() {
        httpRequest(POST, "/login", parseUser(savedUserDto)).isFound();
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidLoginParameters")
    void 로그인_유효성_에러_테스트(String name, String email, String password, String errorMsg) {
        String loginFailRedirectUrl = getRedirectUrl(httpRequest(POST, "/login", parseUser(new UserDto(name, email, password))));
        assertThat(loginFailRedirectUrl).isEqualTo("/login");
    }

    static Stream<Arguments> invalidLoginParameters() {
        return Stream.of(
                Arguments.of(null, "e@mail.com", "p@sswsavedPassw0RD!", LOGIN_FAIL_MESSAGE),
                Arguments.of(null, "saved@email.com", "edPassw0RD!", LOGIN_FAIL_MESSAGE)
        );
    }

    @Test
    void 로그인_상태_로그아웃_성공() {
        loginAndRequest(savedUserDto, GET, "/logout").isFound();
    }

    @Test
    void 로그아웃_상태_로그아웃_리다이렉트() {
        String logoutRedirectUrl = getRedirectUrl(httpRequest(GET, "/logout"));
        assertEquals(logoutRedirectUrl, "/login");
    }
}