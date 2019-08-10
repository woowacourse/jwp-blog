package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.domain.User.AUTH_FAIL_MESSAGE;
import static techcourse.myblog.dto.UserDto.*;
import static techcourse.myblog.service.UserService.*;

class UserControllerTests extends ControllerTestTemplate {
    @Autowired
    private UserRepository userRepository;

    private String name;
    private String email;
    private String password;
    private UserDto savedUserDto;

    @BeforeEach
    void setup() {
        name = "name";
        email = "email@email.com";
        password = "passw0RD!";
        savedUserDto = new UserDto("savedName", "saved@email.com", "savedPassw0RD!");
        userRepository.save(savedUserDto.toUser());
    }

    @Test
    void 로그아웃상태_회원가입_페이지_요청() {
        requestExpect(GET, "/signup").isOk();
    }

    @Test
    void 로그인_상태에서_회원가입_요청시_리다이렉트() {
        loginAndRequest(GET, "/signup").isFound();
    }

    @Test
    void 회원가입_성공시_리다이렉트() {
        UserDto userDto = new UserDto(name, email, password);
        requestExpect(POST, "/users", parser(userDto)).isFound();
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    void 회원가입_유효성_에러_테스트(String name, String email, String password, String errorMsg) {
        requestExpect(POST, "/users", parser(new UserDto(name, email, password)))
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(requestExpect(GET, getRedirectedUri(response)).isOk(),
                                Arrays.asList(errorMsg))
                );
    }

    static Stream<Arguments> invalidParameters() throws Throwable {
        return Stream.of(
                Arguments.of("wrong!name", "e@mail.com", "p@ssw0RD!", NAME_CONSTRAINT_MESSAGE),
                Arguments.of("name", "wrong", "p@ssw00RD!", EMAIL_CONSTRAINT_MESSAGE),
                Arguments.of("name", "e@mail.com", "잘못된패스워드", PASSWORD_CONSTRAINT_MESSAGE),
                Arguments.of("name", "saved@email.com", "passw0RD!", DUPLICATED_USER_MESSAGE)
        );
    }

    @Test
    void 회원목록_페이지_요청() {
        requestExpect(GET, "/users").isOk();
    }

    @Test
    void 로그아웃상태_로그인_페이지_요청() {
        requestExpect(GET, "/login").isOk();
    }

    @Test
    void 로그인_상태에서_로그인_페이지_요청시_리다이렉트() {
        loginAndRequest(GET, "/login").isFound();
    }

    @Test
    void 로그인_성공_시_메인_화면으로_리다이렉트() {
        requestExpect(POST, "/login", parser(savedUserDto))
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        getRedirectedUri(response).equals("/"));
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidLoginParameters")
    void 로그인_유효성_에러_테스트(String name, String email, String password, String errorMsg) {
        requestExpect(POST, "/login", parser(new UserDto(name, email, password)))
                .isFound()
                .expectBody()
                .consumeWith(response ->
                        bodyCheck(requestExpect(GET, getRedirectedUri(response)).isOk(),
                                Arrays.asList(errorMsg))
                );
    }

    static Stream<Arguments> invalidLoginParameters() throws Throwable {
        return Stream.of(
                Arguments.of(null, "e@mail.com", "p@sswsavedPassw0RD!", NOT_EXIST_USER_MESSAGE),
                Arguments.of(null, "saved@email.com", "edPassw0RD!", AUTH_FAIL_MESSAGE)
        );
    }

    @Test
    void 로그아웃_요청() {
        loginAndRequest(GET, "/logout").isFound();
    }

    @Test
    void 로그아웃상태_회원정보페이지_요청_리다이렉트() {
        requestExpect(GET, "/mypage").isFound();
    }

    @Test
    void 로그인_상태에서_회원정보페이지_요청시_성공() {
        loginAndRequest(GET, "/mypage").isOk();
    }

    @Test
    void 로그아웃상태_회원_정보_수정_페이지_요청_리다이렉트() {
        requestExpect(GET, "/mypage/edit").isFound();
    }

    @Test
    void 로그인_상태에서_정보수정페이지_요청시_성공() {
        loginAndRequest(GET, "/mypage/edit").isOk();
    }

    @Test
    void 로그인_상태에서_정보수정_결과_확인() {
        UserDto update = new UserDto("newname", savedUserDto.getEmail(), savedUserDto.getPassword());
        loginAndRequest(PUT, "/mypage", parser(update)).isFound();

        User savedUser = userRepository.findByEmail(savedUserDto.getEmail()).get();
        assertThat(savedUser.getName().equals(update.getName())).isTrue();
        assertThat(savedUser.getName().equals(savedUserDto.getName())).isFalse();
    }

    @Test
    void 로그아웃상태_정보수정_불가() {
        UserDto update = new UserDto("newname", savedUserDto.getEmail(), savedUserDto.getPassword());
        requestExpect(PUT, "/mypage", parser(update)).isFound();

        User savedUser = userRepository.findByEmail(savedUserDto.getEmail()).get();
        assertThat(savedUser.getName().equals(update.getName())).isFalse();
        assertThat(savedUser.getName().equals(savedUserDto.getName())).isTrue();
    }

    @Test
    void 로그아웃상태_탈퇴시도_실패() {
        requestExpect(DELETE, "/users/" + savedUserDto.getEmail()).isFound();
        assertThat(userRepository.findByEmail(savedUserDto.getEmail()).isPresent()).isTrue();
    }

    @Test
    void 로그인상태_자기자신_탈퇴시도_성공() {
        loginAndRequest(DELETE, "/users").isFound();
        assertThat(userRepository.findByEmail(savedUserDto.getEmail()).isPresent()).isFalse();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private StatusAssertions loginAndRequest(HttpMethod method, String path, MultiValueMap<String, String> data) {
        return requestExpect(
                makeRequestSpec(method, path, data)
                        .cookie("JSESSIONID", getLoginSessionId()));
    }

    private StatusAssertions loginAndRequest(HttpMethod method, String path) {
        return requestExpect(
                makeRequestSpec(method, path)
                        .cookie("JSESSIONID", getLoginSessionId()));
    }

    private String getLoginSessionId() {
        return Objects.requireNonNull(requestExpect(POST, "/login", parser(savedUserDto))
                .isFound()
                .returnResult(Void.class)
                .getResponseCookies()
                .getFirst("JSESSIONID"))
                .getValue();
    }

    private MultiValueMap<String, String> parser(UserDto userDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("email", userDto.getEmail());
        multiValueMap.add("name", userDto.getName());
        multiValueMap.add("password", userDto.getPassword());
        return multiValueMap;
    }
}