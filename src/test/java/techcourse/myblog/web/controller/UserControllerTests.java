package techcourse.myblog.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.web.controller.common.ControllerTestTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.service.UserWriteService.DUPLICATED_USER_MESSAGE;
import static techcourse.myblog.validation.UserPattern.*;

class UserControllerTests extends ControllerTestTemplate {
    private String savedUserUrl;

    @Test
    void 회원목록_페이지_요청() {
        httpRequest(GET, "/users").isOk();
    }

    @Test
    void 로그아웃상태_회원가입_페이지_요청() {
        httpRequest(GET, "/signup").isOk();
    }

    @Test
    void 로그인_상태에서_회원가입_요청시_리다이렉트() {
        loginAndRequest(savedUserDto, GET, "/signup").isFound();
    }

    @Test
    void 회원가입_성공시_리다이렉트() {
        UserDto signupUserDto = new UserDto("signUp", "signUp@mail.com", "Passw0rd!");
        httpRequest(POST, "/users", parseUser(signupUserDto)).isFound();
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    void 회원가입_유효성_에러_테스트(String name, String email, String password, String errorMsg) {
        String signupFailRedirectUrl = getRedirectUrl(httpRequest(POST, "/users", parseUser(new UserDto(name, email, password))));
        assertThat(signupFailRedirectUrl).isEqualTo("/signup");
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("wrong!name", "e@mail.com", "p@ssw0RD!", NAME_CONSTRAINT_MESSAGE),
                Arguments.of("name", "wrong", "p@ssw00RD!", EMAIL_CONSTRAINT_MESSAGE),
                Arguments.of("name", "e@mail.com", "잘못된패스워드", PASSWORD_CONSTRAINT_MESSAGE),
                Arguments.of("name", savedUserDto.getEmail(), "passw0RD!", DUPLICATED_USER_MESSAGE)
        );
    }

    @Test
    void 로그아웃상태_회원정보페이지_요청_리다이렉트() {
        httpRequest(GET, "/mypage").isFound();
    }

    @Test
    void 로그인_상태에서_회원정보페이지_요청시_성공() {
        loginAndRequest(savedUserDto, GET, "/mypage").isOk();
    }

    @Test
    void 로그아웃상태_회원_정보_수정_페이지_요청_리다이렉트() {
        httpRequest(GET, "/mypage/edit").isFound();
    }

    @Test
    void 로그인_상태에서_정보수정페이지_요청시_성공() {
        loginAndRequest(savedUserDto, GET, "/mypage/edit").isOk();
    }

    @Test
    void 로그인_상태에서_정보수정_결과_확인() {
        UserDto updateUserDto = new UserDto("newname", savedUserDto.getEmail(), savedUserDto.getPassword());
        loginAndRequest(savedUserDto, PUT, getSavedUserUrl(), parseUser(updateUserDto)).isFound();

        User afterUpdateUser = userRepository.findByEmail(savedUserDto.getEmail()).get();
        assertThat(afterUpdateUser.getName())
                .isEqualTo(updateUserDto.getName())
                .isNotEqualTo(savedUserDto.getName());
    }

    @Test
    void 로그아웃상태_정보수정_불가() {
        UserDto updateUserDto = new UserDto("newname", savedUserDto.getEmail(), savedUserDto.getPassword());
        httpRequest(PUT, getSavedUserUrl(), parseUser(updateUserDto)).isFound();

        User afterUpdateUser = userRepository.findByEmail(savedUserDto.getEmail()).get();
        assertThat(afterUpdateUser.getName())
                .isEqualTo(savedUserDto.getName())
                .isNotEqualTo(updateUserDto.getName());
    }

    @Test
    void 로그아웃상태_탈퇴시도_실패() {
        String logoutDeleteUserRedirectUrl = getRedirectUrl(httpRequest(DELETE, getSavedUserUrl()));
        assertEquals(logoutDeleteUserRedirectUrl, "/login");
        assertThat(userRepository.findByEmail(savedUserDto.getEmail())).isPresent();
    }

    @Test
    void 로그인상태_자기자신_탈퇴시도_성공() {
        String loginDeleteSelfRedirectUrl = getRedirectUrl(loginAndRequest(savedUserDto, DELETE, getSavedUserUrl()));
        assertEquals(loginDeleteSelfRedirectUrl, "/logout");
        assertThat(userRepository.findByEmail(savedUserDto.getEmail())).isNotPresent();
    }

    @Test
    void 로그인상태_다른사용자_탈퇴시도_실패() {
        String loginDeleteSelfRedirectUrl = getRedirectUrl(loginAndRequest(otherUserDto, DELETE, getSavedUserUrl()));
        assertEquals(loginDeleteSelfRedirectUrl, "/");
        assertThat(userRepository.findByEmail(savedUserDto.getEmail())).isPresent();
    }

    private String getSavedUserUrl() {
        if (savedUserUrl == null) {
            savedUserUrl = String.format("/users/%d", savedUser.getId());
        }
        return savedUserUrl;
    }
}