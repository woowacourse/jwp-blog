package techcourse.myblog;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import techcourse.myblog.web.ControllerTest;

public class HelloWorldControllerTest extends ControllerTest {


    @Test
    void passParamWithGetAndResponseBody() {

        String blogName = "helloWorld";
        webTestClient.get().uri("/helloworld?blogName=" + blogName)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(new String(response.getResponseBody())).isEqualTo(blogName));
    }

    @Test
    void passParamWithGet() {
        String blogName = "helloWorld";
        webTestClient.get().uri("/helloworld2?blogName=" + blogName)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void passParamWithPost() {
        String blogName = "helloWorld";
        webTestClient.post()
                .uri("/helloworld")
                .body(Mono.just(blogName), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(new String(response.getResponseBody())).isEqualTo(blogName));
    }
}