package techcourse.myblog.web;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    private static String title = "TEST";
    private static String coverUrl = "https://img.com";
    private static String contents = "testtest";
    private static String categoryId = "1";

    private static final String name = "미스터코";
    private static final String email = "test@test.com";
    private static final String password = "123123123";

    @Autowired
    private WebTestClient webTestClient;

    private String JSSESIONID;

    @BeforeEach
    void setUp() {
        if (JSSESIONID == null) {
            JSSESIONID = getJSSESIONID(name, email, password);
        }
    }

    @Test
    void 댓글_쓰기() {
        webTestClient.post().uri("/comments/1")
                .cookie("JSESSIONID", JSSESIONID)
                .body(createFormData(CommentDto.class, "test"))
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    assertThat(Long.parseLong(s.split("/")[4])).isEqualTo(1);
                });
    }

    @Test
    void 댓글_수정() {
        webTestClient.put().uri("/comments/1/1")
                .cookie("JSESSIONID", JSSESIONID)
                .body(createFormData(CommentDto.class, "test"))
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    assertThat(Long.parseLong(s.split("/")[4])).isEqualTo(1);
                });
    }

    @Test
    void 댓글_삭제() {
        webTestClient.delete().uri("/comments/1/2")
                .cookie("JSESSIONID", JSSESIONID)
                .exchange()
                .expectHeader()
                .value("location", s -> {
                    assertThat(Long.parseLong(s.split("/")[4])).isEqualTo(1);
                });
    }

    public <T> BodyInserters.FormInserter<String> createFormData(Class<T> classType, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);
        for (int i = 1; i < classType.getDeclaredFields().length; i++) {
            if (classType.getDeclaredFields()[i].getName() != "userDto") {
                body.with(classType.getDeclaredFields()[i].getName(), parameters[i - 1]);
            }
        }

        return body;
    }

    public String getJSSESIONID(String name, String email, String password) {
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