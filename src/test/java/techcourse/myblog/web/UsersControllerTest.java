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
class UsersControllerTest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getSignUpPage() {
		webTestClient.get().uri("/signup")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void joinSuccess() {
		webTestClient.post()
				.uri("/users")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("username", "tiber")
						.with("email", "tiber@naver.com")
						.with("password", "asdfASDF1@"))
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/login");
	}

	@Test
	void joinFailureDueToUsernameValue() {
		webTestClient.post()
				.uri("/users")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("username", "t")
						.with("email", "tiber@naver.com")
						.with("password", "asdfASDF1@"))
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void joinFailureDueToEmailValue() {
		webTestClient.post()
				.uri("/users")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("username", "tiber")
						.with("email", "tibernaver.com")
						.with("password", "asdfASDF1@"))
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void joinFailureDueToPasswordValue() {
		webTestClient.post()
				.uri("/users")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("username", "tiber")
						.with("email", "tiber@naver.com")
						.with("password", "aB1"))
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void users() {
		webTestClient.get()
				.uri("/users")
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/user-list");
	}

	@Test
	void userList() {
		webTestClient.get()
				.uri("/user-list")
				.exchange()
				.expectStatus()
				.isOk();
	}
}