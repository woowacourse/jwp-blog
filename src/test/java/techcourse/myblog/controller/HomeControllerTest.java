package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.model.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

    private static final String USER_NAME = "test1";
    private static final String EMAIL = "test1@test.com";
    private static final String PASSWORD = "1234";

    protected MockHttpSession session;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> exchange.getSession()
                .doOnNext(webSession -> webSession.getAttributes().put("user", new User(USER_NAME, EMAIL, PASSWORD))).then())
                .build();
    }

    @Test
    void 로그인_폼_테스트() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    try {
                        String body = new String(response.getResponseBody(), "UTF-8");
                        assertThat(body.contains(USER_NAME)).isTrue();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
    }
}
