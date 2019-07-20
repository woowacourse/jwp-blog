package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private UserDto userDto;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");
    }

    @Test
    @DisplayName("회원 가입 후 로그인 페이지로 이동한다.")
    void signUpTest() {
        userDto.setName("signup");
        userDto.setEmail("new@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        postUser(userDto).is3xxRedirection();
    }

    @Test
    @DisplayName("회원 가입 요청시 잘못된 이름 패턴으로 인해 실패한다.")
    void notSignUpNameTest() {
        userDto.setName("test1");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        postUser(userDto).isOk();
    }

    @Test
    @DisplayName("회원 가입 요청시 잘못된 이메일 패턴으로 인해 실패한다.")
    void notSignUpByEmailTest() {
        userDto.setName("test");
        userDto.setEmail("test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        postUser(userDto).isOk();
    }

    @Test
    @DisplayName("회원 가입 요청시 잘못된 패스워드 패턴으로 인해 실패한다.")
    void notSignUpByPasswordTest() {
        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassWord");
        userDto.setPasswordConfirm("PassWord");

        postUser(userDto).isOk();
    }

    @Test
    @DisplayName("회원 가입 요청시 두개의 패스워드가 다른 이유로 인해 실패한다.")
    void notSignUpByUnequalPasswordTest() {
        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassWord@");

        postUser(userDto).isOk();
    }

    @Test
    @DisplayName("회원 목록 페이지를 보여주고 DB에 저장된 회원 정보를 노출한다.")
    void showUserListTest() {
        userDto.setName("test");
        userDto.setEmail("user@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        postUser(userDto).is3xxRedirection()
                .expectBody().consumeWith(postUserResponse ->
                webTestClient.get()
                        .uri("/users")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .consumeWith(result -> assertThat(new String(result.getResponseBody()))
                                .contains(userDto.getName())
                                .contains(userDto.getEmail())
                        ));
    }

    private StatusAssertions postUser(UserDto userDto) {
        return webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("passwordConfirm", userDto.getPasswordConfirm()))
                .exchange()
                .expectStatus();
    }
}