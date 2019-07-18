package techcourse.myblog.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.service.UserService;

import static techcourse.myblog.web.UserController.USER_MAPPING_URL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserService userService;

    @Test
    public void loginForm_call_isOk (){
        webTestClient.get()
                .uri(USER_MAPPING_URL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void signUpForm_call_isOk (){
        webTestClient.get()
                .uri(USER_MAPPING_URL+"/signup")
                .exchange()
                .expectStatus()
                .isOk();
    }


}