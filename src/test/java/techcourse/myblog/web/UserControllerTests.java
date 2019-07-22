package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import techcourse.myblog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
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
		getStatusAssertionsForSignup("tiber", "tiber@naver.com", "asdfASDF123@!#$")
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/login");

		assertTrue(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"t", "abcdefghijk", "tiber1", "tiber!"})
	void signUpFailureDueToUsernameValue(String name) {
		getStatusAssertionsForSignup(name, "tiber@naver.com", "asdfASDF1@")
				.isOk();

		assertFalse(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"t", "tibernavercom", "tibernaver.com"})
	void signUpFailureDueToEmailValue(String email) {
		getStatusAssertionsForSignup("tiber", email, "asdfASDF1@")
				.isOk();

		assertFalse(userRepository.findByEmail(email).isPresent());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"aA1!", "12345678", "abcedfgh", "ABCDEFGH", "!@#$%^&*", "aaaAAA111",
			"aaa111!@#", "aaaAAA$%^", "AAA111%^&"})
	void signUpFailureDueToPasswordValue(String password) {
		getStatusAssertionsForSignup("tiber", "tiber@naver.com", password)
				.isOk();

		assertFalse(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	private StatusAssertions getStatusAssertionsForSignup(String username, String email, String password) {
		return webTestClient.post()
				.uri("/users")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("username", username)
						.with("email", email)
						.with("password", password))
				.exchange()
				.expectStatus();
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