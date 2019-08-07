package techcourse.myblog;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void passParamWithGet() {
	String blogName = "helloWrold";
	webTestClient.get().uri("/helloworld?blogName=" + blogName)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.consumeWith(response ->
			Assertions.assertThat(new String(response.getResponseBody())).isEqualTo(blogName));
    }

    @Test
    public void passParamWithPost() {
	String blogName = "helloWrold";

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
