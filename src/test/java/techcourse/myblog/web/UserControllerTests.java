package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import techcourse.myblog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	void getSignUpPage() {
		webTestClient.get().uri("/signup")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void signUpSuccess() {
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

		assertTrue(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	@Test
	void signUpFailureDueToUsernameValue() {
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

		assertFalse(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	@Test
	void signUpFailureDueToEmailValue() {
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

		assertFalse(userRepository.findByEmail("tibernaver.com").isPresent());
	}

	@Test
	void signUpFailureDueToPasswordValue() {
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

		assertFalse(userRepository.findByEmail("tiber@naver.com").isPresent());
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

	@Test
	void moveMyPageFailureDueToNotLogin() {
		webTestClient.get()
				.uri("/mypage")
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/");
	}

	@Test
	void moveMyPageEditFailureDueToNotLogin() {
		webTestClient.get()
				.uri("/mypage/edit")
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/");
	}
}