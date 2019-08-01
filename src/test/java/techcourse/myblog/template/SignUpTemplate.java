package techcourse.myblog.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.article.repository.ArticleRepository;
import techcourse.myblog.user.repository.UserRepository;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpTemplate {
    protected static final String NAME = "이름";
    protected static final String PASSWORD = "Password!123";
    protected static final String EMAIL = "wooteco@wooteco.com";

    @Autowired
    public WebTestClient webTestClient;

    public void registeredWebTestClient() {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", NAME)
                        .with("password", PASSWORD)
                        .with("email", EMAIL))
                .exchange();
    }

    public void registeredWebTestClient(String name, String email, String password) {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange();
    }
}
