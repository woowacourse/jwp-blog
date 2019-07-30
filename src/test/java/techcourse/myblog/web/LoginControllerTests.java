package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTests {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void loginSuccess() {
		requestForLogin("tiber@naver.com", "asdfASDF1@").isFound();
	}

	@Test
	void loginFailureDueToWrongEmail() {
		requestForLogin("ssosso@naver.com", "asdfASDF1@").isBadRequest();
	}

	@Test
	void loginFailureDueToWrongPassword() {
		requestForLogin("tiber@naver.com", "asdfASDF1@!").isBadRequest();
	}

	private StatusAssertions requestForLogin(String email, String password) {
		return webTestClient.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", email)
						.with("password", password))
				.exchange()
				.expectStatus();
	}
}
