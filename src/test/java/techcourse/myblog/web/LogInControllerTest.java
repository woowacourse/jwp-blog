package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;
import static techcourse.myblog.service.exception.LogInException.NOT_FOUND_USER_MESSAGE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogInControllerTest {
	public static final String USER_NAME = "test";
	public static final String USER_EMAIL = "test@test.test";
	public static final String USER_PASSWORD = VALID_PASSWORD;
	private static final String MISMATCH_USER_EMAIL = "test2@test.test";
	private static final String MISMATCH_USER_PASSWORD = VALID_PASSWORD;

	@LocalServerPort
	int randomPortNumber;

	@Autowired
	WebTestClient webTestClient;

	@Test
	@DisplayName("로그인 페이지를 보여준다.")
	public void showLoginPage() {
		webTestClient.get()
				.uri("/login")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	@DisplayName("로그아웃시 메인 화면을 띄운다.")
	public void logOut() {
		webTestClient.get()
				.uri("/logout")
				.exchange()
				.expectStatus().isFound();
	}

	@Test
	@DisplayName("로그인 성공 시 메인 화면을 띄우고 우측 상단에 사용자 이름을 띄운다.")
	public void successLogIn() {
		String jsessiontId = logInAsBaseUser(webTestClient);

		webTestClient.get()
				.uri("/")
				.cookie("JSESSIONID", jsessiontId)
				.exchange()
				.expectBody()
				.consumeWith(res -> {
					String body = new String(res.getResponseBody());
					assertThat(body).contains(USER_NAME);
				});
	}

	@Test
	@DisplayName("없는 이메일로 로그인 했을때 에러 메세지 출력한다.")
	public void failLogIn() {
		String name = "testName";
		String email = "logintest2@woowa.com";
		String password = VALID_PASSWORD;
		String passwordConfirm = VALID_PASSWORD;
		String diffEmail = "diff@woowa.com";

		webTestClient.post()
				.uri("/users")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("name", name)
						.with("email", email)
						.with("password", password)
						.with("passwordConfirm", passwordConfirm))
				.exchange()
				.expectStatus().isFound();

		webTestClient.post()
				.uri("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("email", diffEmail)
						.with("password", password))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertThat(body).contains(NOT_FOUND_USER_MESSAGE);
				});
	}

	public static String logIn(WebTestClient webTestClient, String email, String password) {
		return webTestClient.post().uri("/login")
				.body(BodyInserters.fromFormData("email", email)
						.with("password", password))
				.exchange()
				.returnResult(String.class)
				.getResponseCookies().get("JSESSIONID").get(0).getValue();
	}

	public static String logInAsMismatchUser(WebTestClient webTestClient) {
		return logIn(webTestClient, MISMATCH_USER_EMAIL, MISMATCH_USER_PASSWORD);
	}

	public static String logInAsBaseUser(WebTestClient webTestClient) {
		return logIn(webTestClient, USER_EMAIL, USER_PASSWORD);
	}

	@AfterEach
	public void tearDown() {
		webTestClient.get().uri("/logout");
	}
}