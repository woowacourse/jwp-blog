package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class AbstractControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    EntityExchangeResult<byte[]> login(String email, String password){
        return webTestClient.post().uri("/login")
                .body(fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/.*")
                .expectBody()
                .returnResult();
    }

    String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
        String[] cookies = loginResult.getResponseHeaders().get("set-Cookie").stream()
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst()
                .orElseThrow(()->new RuntimeException("jSessionId가 없습니다."))
                .split(";");
        return Stream.of(cookies)
                .filter(it -> it.contains("JSESSIONID"))
                .findFirst()
                .orElseThrow(()->new RuntimeException("jSessioId가 없습니다."))
                .split("=")[1];
    }

    String getJSessionId(String userName, String email, String password) {
        create_user(userName,email,password);
        EntityExchangeResult<byte[]> login = login(email, password);
        return extractJSessionId(login);
    }

    WebTestClient.ResponseSpec create_user(String userName, String email, String password) {
        return webTestClient.post().uri("/users/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", userName)
                        .with("email", email)
                        .with("password", password)
                        .with("confirmPassword", password)
                ).exchange();
    }
}
