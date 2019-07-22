package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.repository.UserRepository;

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
	//TODO: SESSION TEST 수정 필요
	@Test
	void loginSuccess() {
		requestForLogin("tiber@naver.com", "asdfASDF1@").isFound()
				.expectHeader()
				.valueMatches("Set-Cookie", "JSESSIONID=.+");
	}

	@Test
	void loginFailureDueToWrongEmail() {
		requestForLogin("ssosso@naver.com", "asdfASDF1@").isOk();
	}

	@Test
	void loginFailureDueToWrongPassword() {
		requestForLogin("tiber@naver.com", "wrongPassword").isOk();
	}

	private StatusAssertions requestForLogin(String email, String requestURI) {
		return webTestClient.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", email)
						.with("password", requestURI))
				.exchange()
				.expectStatus();
	}
}
