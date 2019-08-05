package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpTemplate {

    protected static final String NAME = "ike";
    protected static final String PASSWORD = "ASas!@12";
    protected static final String EMAIL = "ike@gmail.com";

    @Autowired
    public WebTestClient webTestClient;

    public void signUpUser() {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", NAME)
                        .with("password", PASSWORD)
                        .with("email", EMAIL))
                .exchange();
    }

    public void signUpUser(String name, String password, String email) {
        webTestClient.post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange();
    }
}
