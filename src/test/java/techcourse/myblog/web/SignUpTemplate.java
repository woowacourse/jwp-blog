package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpTemplate {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;

    void registeredWebTestClient() {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "andole")
                        .with("password", "A!1bcdefg")
                        .with("email", "andole@gmail.com"))
                .exchange();
    }
}
