package techcourse.myblog.web;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.UserDto;

import java.util.function.Consumer;

class ControllerTest {
    static void postUser(WebTestClient webTestClient, UserDto userDto, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/users")
                .body(BodyInserters.fromFormData("name", userDto.getName())
                        .with("email", userDto.getEmail())
                        .with("password", userDto.getPassword())
                        .with("passwordConfirm", userDto.getPasswordConfirm()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    static void postLogin(WebTestClient webTestClient, LoginDto loginDto, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post()
                .uri("/login" + sessionId)
                .body(BodyInserters
                        .fromFormData("email", loginDto.getEmail())
                        .with("password", loginDto.getPassword()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(consumer);
    }

    static void getIndexView(WebTestClient webTestClient, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/" + sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(consumer);
    }

    static void getMyPageView(WebTestClient webTestClient, String sessionId, Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.get()
                .uri("/mypage" + sessionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(consumer);
    }

    static String getSessionId(EntityExchangeResult<byte[]> postUserResponse) {
        return ";jsessionid=" + postUserResponse.getResponseCookies().getFirst("JSESSIONID").getValue();
    }
}
