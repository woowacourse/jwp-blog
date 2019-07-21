package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest {
	private static final String NAME = "yusi";
	private static final String EMAIL = "temp@mail.com";
	private static final String PASSWORD = "12345abc";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private UserService userService;

	@BeforeEach
	public void setUp() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		userService.deleteAll();
		userService.save(signUpRequestDto);
	}

	@Test
	public void 로그인_성공() {
		webTestClient.post().uri("/login")
				.body(BodyInserters
						.fromFormData("email", EMAIL)
						.with("password", PASSWORD)
				)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertTrue(body.contains(NAME));
				});
	}

	@Test
	public void 로그인_실패_이메일이_없을때() {
		final String wrongEmail = "not@mail.com";
		webTestClient.post().uri("/login")
				.body(BodyInserters
						.fromFormData("email", wrongEmail)
						.with("password", PASSWORD)
				)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void 로그인_실패_비밀번호가_다를때() {
		final String wrongPassword = "12345678";

		webTestClient.post().uri("/login")
				.body(BodyInserters
						.fromFormData("email", EMAIL)
						.with("password", wrongPassword)
				)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void 로그아웃_테스트() {
		로그인_성공();
		webTestClient.get().uri("/logout")
				.exchange()
				.expectStatus().isFound();
	}

	@Test
	public void 로그인_페이지_이동() {
		webTestClient.get().uri("/login")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void 회원가입_페이지_이동() {
		webTestClient.get().uri("/signup")
				.exchange()
				.expectStatus().isOk();
	}
}