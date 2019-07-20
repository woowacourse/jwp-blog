package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

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
class LoginControllerTests {
	private User user = new User();

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
		UserDto userDto = new UserDto();
		userDto.setUsername("tiber");
		userDto.setEmail("tiber@naver.com");
		userDto.setPassword("asdfASDF1@");

		user.saveUser(userDto);
		userRepository.save(user);
	}

	@Test
	void loginSuccess() {
		webTestClient.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", "tiber@naver.com")
						.with("password", "asdfASDF1@"))
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Set-Cookie", "JSESSIONID=.+");
	}

	@Test
	void loginFailureDueToWrongEmail() {
		webTestClient.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", "ssosso@naver.com")
						.with("password", "asdfASDF1@"))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.doesNotExist("jsessionid");
	}

	@Test
	void loginFailureDueToWrongPassword() {
		webTestClient.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", "tiber@naver.com")
						.with("password", "wrongPassword"))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.doesNotExist("jsessionid");
	}
}