package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.presentation.controller.UserController.USER_MAPPING_URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    UserDto userDto;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setup(){
        cookie = getCookie();
    }

    @Test
    void loginForm_get_isOk() {
        webTestClient.get()
                .uri(USER_MAPPING_URL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void signUpForm_get_isOk() {
        webTestClient.get()
                .uri(USER_MAPPING_URL + "/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void signUp_post_is3xxRedirect() {
        signupRequest("kangmin7@abc.com", "abc", "asdASD12!@")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void logout_post_is3xxRedirect() {
        userDto = new UserDto("kangmin789@abc.com", "abc", "asdASD12!@");
        commonRequest(POST, userDto, "/logout")
                .expectStatus()
                .is3xxRedirection();
    }

    @Test
    void login_wrongId_redirectToMain() {

    }


    private WebTestClient.ResponseSpec commonRequest(HttpMethod method, final UserDto userDto, String attachedUrl) {
        return webTestClient.method(method)
                .uri(USER_MAPPING_URL + attachedUrl)
                .body(fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange();
    }

    private WebTestClient.ResponseSpec signupRequest(String email, String name, String password) {
        UserDto userDto = UserDto.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        return commonRequest(POST, userDto, "");
    }

    private WebTestClient.ResponseSpec loginRequest(String email, String password) {
        LoginDto loginDto = LoginDto.builder()
                .email(email)
                .password(password)
                .build();
        return webTestClient.put()
                .uri(USER_MAPPING_URL + "/login")
                .body(fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange();
    }

    private String getCookie() {
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", "")
                        .with("adg", "adg"))
                .exchange()
                .expectStatus()
                .isFound()
                .returnResult(String.class)
                .getResponseHeaders()
                .getFirst("Set-Cookie");
    }
}