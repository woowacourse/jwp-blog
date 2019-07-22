package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.UserDto;

import static org.springframework.http.HttpMethod.POST;
import static techcourse.myblog.presentation.controller.UserController.USER_MAPPING_URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    UserDto userDto;
    @Autowired
    WebTestClient webTestClient;

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
        userDto = new UserDto("kangmin789@abc.com", "abc", "asdASD12!@");
        userRequest(POST,userDto, "").expectStatus().is3xxRedirection();

    }

    @Test
    void logout_post_is3xxRedirect() {
        userDto = new UserDto("kangmin789@abc.com", "abc", "asdASD12!@");
        userRequest(POST,userDto , "/logout")
                .expectStatus()
                .is3xxRedirection();

    }

    private WebTestClient.ResponseSpec userRequest(HttpMethod method, final UserDto userDto, String attachedUrl) {
        return webTestClient.method(method)
                .uri(USER_MAPPING_URL + attachedUrl)
                .body(BodyInserters
                        .fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword()))
                .exchange();
    }


}