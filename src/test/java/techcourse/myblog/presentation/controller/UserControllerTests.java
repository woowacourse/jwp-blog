package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;
import static techcourse.myblog.presentation.controller.ControllerTestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Autowired
    WebTestClient webTestClient;

    @LocalServerPort
    String port;

    private UserDto userDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("zino");
        userDto.setEmail("email2@zino.me");
        userDto.setPassword("password");

        loginDto = new LoginDto();
        loginDto.setEmail("email2@zino.me");
        loginDto.setPassword("password");
    }

    @Test
    void 회원가입_후_로그인_테스트() {
        postUser(webTestClient, userDto, postResponseUser -> {
            webTestClient.post()
                    .uri("/login")
                    .body(BodyInserters.fromFormData("email", loginDto.getEmail())
                            .with("password", loginDto.getPassword()))
                    .exchange()
                    .expectStatus().is3xxRedirection()
                    .expectHeader().valueMatches("Location", "http://localhost:" + port + "/.*");
        });
    }

    @Test
    void 로그아웃_테스트() {
        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                webTestClient.get()
                        .uri("/logout" + sessionId)
                        .exchange()
                        .expectStatus().is3xxRedirection();
            });
        });
    }

    @Test
    void myPage_조회_테스트() {
        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                webTestClient.get()
                        .uri("/mypage" + sessionId)
                        .exchange()
                        .expectStatus().isOk();
            });
        });

    }

    @Test
    void myPage_수정_페이지_테스트() {
        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                webTestClient.get()
                        .uri("/mypage/edit" + sessionId)
                        .exchange()
                        .expectStatus().isOk();
            });
        });
    }

    @Test
    void myPage_수정_테스트() {
        postUser(webTestClient, userDto, postResponseUser -> {
            loginUser(webTestClient, loginDto, postResponseLogin -> {
                String sessionId = getSessionId(postResponseLogin);
                webTestClient.put()
                        .uri("/mypage/edit" + sessionId)
                        .body(fromFormData("email", "zino@naver.com")
                                .with("password", "zino123!@#")
                                .with("name", "zinozino"))
                        .exchange()
                        .expectStatus().isFound();
            });
        });
    }
}
