package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserDto;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.web.LoginControllerTest.getSessionId;
import static techcourse.myblog.web.LoginControllerTest.postLogin;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private UserDto userDto;
    private LoginDto loginDto;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        loginDto = new LoginDto();

        userDto.setName("test");
        userDto.setEmail("test@test.com");
        userDto.setPassword("PassW0rd@");
        userDto.setPasswordConfirm("PassW0rd@");

        loginDto.setEmail("test@test.com");
        loginDto.setPassword("PassW0rd@");
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

    public static void postUser(WebTestClient webTestClient, UserDto userDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("passwordConfirm", userDto.getPasswordConfirm()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    private void getMyPageView(String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/mypage" + sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(consumer);
    }

    public static void getIndexView(WebTestClient webTestClient, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/" + sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(consumer);
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

        postUser(webTestClient, userDto, postUserResponse -> {
            webTestClient.get()
                    .uri("/users")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .consumeWith(result -> assertThat(new String(result.getResponseBody()))
                            .contains(userDto.getName())
                            .contains(userDto.getEmail()));
        });
    }

    @Test
    @DisplayName("로그인을 유지하고 회원 정보 페이지에서 유저의 이름과 이메일을 확인한다.")
    void myPageViewTest() {
        userDto.setEmail("mypage@test.com");
        loginDto.setEmail("mypage@test.com");

        postUser(webTestClient, userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);
            postLogin(webTestClient, loginDto, sessionId, postLoginResponse -> {
                getMyPageView(sessionId, myPage ->
                        assertThat(new String(myPage.getResponseBody()))
                                .contains("test")
                                .contains("mypage@test.com"));
            });
        });
    }

    @Test
    @DisplayName("회원 정보를 수정하고 mypage에서 수정된 이름을 확인한다.")
    void editMyPageTest() {
        String updateName = "kimhyojae";
        userDto.setEmail("edit@test.com");
        loginDto.setEmail("edit@test.com");

        postUser(webTestClient, userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);
            postLogin(webTestClient, loginDto, sessionId, postLoginResponse -> {
                webTestClient.put()
                        .uri("/mypage-edit" + sessionId)
                        .body(BodyInserters.fromFormData("name", updateName))
                        .exchange()
                        .expectStatus().is3xxRedirection()
                        .expectBody()
                        .consumeWith(updateUser ->
                                getMyPageView(sessionId, myPage -> assertThat(new String(myPage.getResponseBody()))
                                        .contains(updateName)));
            });
        });
    }

    @Test
    @DisplayName("회원 정보 수정 시 잘못된 이름을 입력하면 에러 메시지를 띄웁니다.")
    void editMyPageFailTest() {
        String invalidName = "123";
        userDto.setEmail("fail@test.com");
        loginDto.setEmail("fail@test.com");

        postUser(webTestClient, userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);
            postLogin(webTestClient, loginDto, sessionId, postLoginResponse -> {
                webTestClient.put()
                        .uri("/mypage-edit" + sessionId)
                        .body(BodyInserters.fromFormData("name", invalidName))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .consumeWith(test -> assertThat(new String(test.getResponseBody()))
                                .contains("이름은 한글 또는 영어만 입력이 가능합니다."));
            });
        });
    }

    @Test
    @DisplayName("회원탈퇴 후에 메인 페이지에 비로그인 상태인지 확인한다.")
    void withdrawalTest() {
        userDto.setName("김효재");
        userDto.setEmail("delete@test.com");
        loginDto.setEmail("delete@test.com");

        postUser(webTestClient, userDto, postUserResponse -> {
            String sessionId = getSessionId(postUserResponse);
            postLogin(webTestClient, loginDto, sessionId, postLoginResponse -> {
                webTestClient.delete()
                        .uri("/withdrawal" + sessionId)
                        .exchange()
                        .expectBody()
                        .consumeWith(withdrawal -> getIndexView(webTestClient, sessionId, index ->
                                assertThat(new String(index.getResponseBody())).doesNotContain("김효재")));
            });
        });
    }
}