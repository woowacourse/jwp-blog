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
import org.springframework.http.HttpMethod;
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
	void users() {
		StatusAssertions statusAssertions = request(HttpMethod.GET, "/users");
		checkRedirect(statusAssertions, "Location", ".+/user-list");
	}

	@Test
	void userList() {
		request(HttpMethod.GET, "/user-list").isFound();
	}

	@Test
	void getSignUpPage() {
		request(HttpMethod.GET, "/signup").isOk();
	}

	@Test
	void signUpSuccess() {
		StatusAssertions statusAssertions = requestForSignUp("tiber", "tiber@naver.com", "asdfASDF123@!#$");
		checkRedirect(statusAssertions, "Location", ".+/login");
		assertTrue(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"t", "abcdefghijk", "tiber1", "tiber!"})
	void signUpFailureDueToUsernameValue(String name) {
		requestForSignUp(name, "tiber@naver.com", "asdfASDF1@").isOk();
		assertFalse(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"t", "tibernavercom", "tibernaver.com"})
	void signUpFailureDueToEmailValue(String email) {
		requestForSignUp("tiber", email, "asdfASDF1@").isOk();
		assertFalse(userRepository.findByEmail(email).isPresent());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"aA1!", "12345678", "abcedfgh", "ABCDEFGH", "!@#$%^&*", "aaaAAA111",
			"aaa111!@#", "aaaAAA$%^", "AAA111%^&"})
	void signUpFailureDueToPasswordValue(String password) {
		requestForSignUp("tiber", "tiber@naver.com", password).isOk();
		assertFalse(userRepository.findByEmail("tiber@naver.com").isPresent());
	}

	private StatusAssertions requestForSignUp(String username, String email, String password) {
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

	private StatusAssertions request(HttpMethod httpMethod, String requestURI) {
		return webTestClient.method(httpMethod)
				.uri(requestURI)
				.exchange()
				.expectStatus();
	}

	private void checkRedirect(StatusAssertions statusAssertions, String name, String redirectURLRegex) {
		statusAssertions.isFound()
				.expectHeader()
				.valueMatches(name, redirectURLRegex);
	}
}