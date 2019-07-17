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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    private String name, email, password;

    @BeforeEach
    void setup() {
        name = "name";
        email = "email@email";
        password = "password";
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

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), BAD_REQUEST);
    }

    @Test
    void 이메일_형식_위반() {
        email = "email";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), BAD_REQUEST);
    }

    @Test
    void 비밀번호_길이_규칙_위반() {
        password = "1234567";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), BAD_REQUEST);
    }

    @Test
    void 동일_이메일_중복_가입() {
        email = "newemail@email";
        UserDto userDto = new UserDto(name, email, password);

        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), FOUND);
        httpRequestAndExpectStatus(POST, "/users", createUserForm(userDto), INTERNAL_SERVER_ERROR);
    }

    @Test
    void 회원목록_페이지() {
        httpRequestAndExpectStatus(GET, "/users", OK);
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
}