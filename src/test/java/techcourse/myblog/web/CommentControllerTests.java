package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {
	@Autowired
	private WebTestClient webTestClient;

	private String getCookie() {
		return webTestClient
				.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", "tiber@naver.com")
						.with("password", "asdfASDF1@"))
				.exchange()
				.returnResult(String.class)
				.getResponseHeaders()
				.getFirst("Set-Cookie");
	}

	@Test
	void saveComment() {
		webTestClient.post()
				.uri("/comments/" + 1)
				.header("Cookie", getCookie())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("contents", "contents"))
				.exchange()
				.expectStatus()
				.isFound();
	}

	@Test
	void updateComment() {
		webTestClient.put()
				.uri("/comments/1/1")
				.header("Cookie", getCookie())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("contents", "updateContents"))
				.exchange()
				.expectStatus()
				.isFound();
	}

	@Test
	void deleteComment() {
		webTestClient.delete()
				.uri("/comments/1/2")
				.header("Cookie", getCookie())
				.exchange()
				.expectStatus()
				.isFound();
	}
}
