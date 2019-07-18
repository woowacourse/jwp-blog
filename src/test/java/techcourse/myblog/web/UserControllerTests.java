package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void 회원등록_확인() {
        String name = "done";
        String email = "gch01410@woowa.com";
        String password = "1234";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
