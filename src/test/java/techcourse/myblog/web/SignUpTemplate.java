package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

public class SignUpTemplate {
    @Autowired
    private WebTestClient webTestClient;

    public WebTestClient getRegisteredWebTestClient() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "andole")
                        .with("password", "A!1bcdefg")
                        .with("email", "andole@gmail.com"))
                .exchange();

        return webTestClient;
    }
}
