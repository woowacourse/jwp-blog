package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.UserDto;

import static techcourse.myblog.web.UserController.USER_MAPPING_URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    UserDto userDto;
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("abc","abc@abc.com","asdASD12!@");
    }

    @Test
    void loginForm_call_isOk() {
        webTestClient.get()
                .uri(USER_MAPPING_URL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void signUpForm_call_isOk() {
        webTestClient.get()
                .uri(USER_MAPPING_URL + "/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void signUp_call_ikOk() {
        webTestClient.post()
                .uri(USER_MAPPING_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}