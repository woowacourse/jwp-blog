package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginControllerTest {
    private static final String email = "test@test.com";
    private static final String password = "123123123";

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int localServerPort;

    private String JSSESIONID;

    @BeforeEach
    void setUp() {
        if (JSSESIONID == null) {
            JSSESIONID = getJSSESIONID();
        }
    }

    @Test
    public void 회원가입접근() {
        webTestClient.get()
                .uri("/signup")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/");
    }

    @Test
    public void 회원가입() {
        webTestClient.post()
                .uri("/signup")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/");
    }

    @Test
    public void 회원로그인접근() {
        webTestClient.get()
                .uri("/login")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/");
    }

    @Test
    public void 회원로그인() {
        webTestClient.post()
                .uri("/login")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange()
                .expectHeader()
                .valueEquals("location", "http://localhost:" + localServerPort + "/");
    }

    @Test
    public void 회원조회페이지_이동() {
        webTestClient.get()
                .uri("/users")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 유저페이지() {
        webTestClient.get()
                .uri("/users/mypage")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange().expectStatus().isOk();
    }

    private String getJSSESIONID() {
        List<String> result = new ArrayList<>();

        webTestClient.post()
                .uri("/login")
                .body(BodyInserters
                        .fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectHeader()
                .value("Set-Cookie", cookie -> result.add(cookie));

        return Stream.of(result.get(0).split(";"))
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(""))
                .split("=")[1];
    }
}
