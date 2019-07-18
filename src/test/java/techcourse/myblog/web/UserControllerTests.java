package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserDto;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private String name;
    private String email;
    private String password;

    @BeforeEach
    void setup() {
        name = "name";
        email = "email@email";
        password = "password";
    }

    @Test
    void 로그인_페이지_요청() {
        httpRequestAndExpectStatus(GET, "/login", OK);
    }

    @Test
    void 회원목록_페이지_요청() {
        httpRequestAndExpectStatus(GET, "/users", OK);
    }

    @Test
    void 회원가입_페이지_요청() {
        httpRequestAndExpectStatus(GET, "/signup", OK);
    }

    @Test
    void 회원가입_성공() {
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), FOUND);
    }

    @Test
    void 이름_길이_규칙_위반() {
        name = "k";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), OK)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    body.contains("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.");
                });
    }

    @Test
    void 이메일_형식_위반() {
        email = "email";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), OK)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains("이메일 양식을 지켜주세요.")).isTrue();
                });
    }

    @Test
    void 비밀번호_길이_규칙_위반() {
        password = "1234567";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), OK)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")).isTrue();
                });
    }

    @Test
    void 동일_이메일_중복_가입() {
        email = "newemail@email";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), FOUND);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), OK)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains("이미 존재하는 email입니다.")).isTrue();
                });
    }

    @Test
    void 회원목록_페이지() {
        httpRequestAndExpectStatus(GET, "/users", OK);
    }

    @Test
    void 로그인_성공_시_메인_화면으로_리다이렉트() {
        final String forLoginEmail = "login@login.com";

        UserDto userDto = new UserDto(name, forLoginEmail, password);
        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), FOUND);

        httpRequestAndExpectStatus(POST, "/login", createLoginForm(userDto), FOUND);
    }

    @Test
    void 해당_이메일이_없는_경우_에러() {
        final String notSignedUpEmail = "thisisfake@login.com";

        UserDto userDto = new UserDto(name, notSignedUpEmail, password);

        httpRequestAndExpectStatus(POST, "/login", createLoginForm(userDto), OK)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains("이메일을 확인해주세요.")).isTrue();
                });
    }

    @Test
    void 비밀번호_불일치하는_경우_에러() {
        final String forLoginEmail = "login@login.com";

        UserDto userDto = new UserDto(name, forLoginEmail, password);
        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), FOUND);

        UserDto userDtoWrongPassword = new UserDto(name, forLoginEmail, "abcdeghf");
        httpRequestAndExpectStatus(POST, "/login", createLoginForm(userDtoWrongPassword), OK)
                .expectBody()
                .consumeWith(res -> {
                    String body = new String(Objects.requireNonNull(res.getResponseBody()));
                    assertThat(body.contains("비밀번호를 확인해주세요.")).isTrue();
                });
    }

    private WebTestClient.ResponseSpec httpRequestAndExpectStatus(HttpMethod method, String uri, HttpStatus status) {
        return httpRequestAndExpectStatus(method, uri, null, status);
    }

    private WebTestClient.ResponseSpec httpRequestAndExpectStatus(HttpMethod method, String uri,
                                                                  BodyInserters.FormInserter<String> form, HttpStatus status) {
        return webTestClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .exchange()
                .expectStatus().isEqualTo(status);
    }

    private BodyInserters.FormInserter<String> createUserForm(UserDto userDto) {
        return BodyInserters
                .fromFormData("name", userDto.getName())
                .with("email", userDto.getEmail())
                .with("password", userDto.getPassword());
    }

    private BodyInserters.FormInserter<String> createLoginForm(UserDto userDto) {
        return BodyInserters
                .fromFormData("email", userDto.getEmail())
                .with("password", userDto.getPassword());
    }
}