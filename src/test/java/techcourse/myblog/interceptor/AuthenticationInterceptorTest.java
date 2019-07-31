package techcourse.myblog.interceptor;

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
class AuthenticationInterceptorTest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void moveMyPageAfterLogin() {
		getRequestWithSession("/mypage").isOk();
	}

	@Test
	void canNotMyPageBeforeLogin() {
		getRequest("/mypage").isFound()
				.expectHeader()
				.valueMatches("Location", ".+/login");
	}

	@Test
	void moveMyPageEditAfterLogin() {
		getRequestWithSession("/mypage/edit").isOk();
	}

	@Test
	void canNotMyPageEditBeforeLogin() {
		StatusAssertions statusAssertions = getRequest("/mypage/edit");
		checkRedirect(statusAssertions, "Location", ".+/login");
	}

	@Test
	void moveLeaveUserPageAfterLogin() {
		getRequestWithSession("/leave").isOk();
	}

	@Test
	void canNotMoveLeaveUserPageBeforeLogin() {
		StatusAssertions statusAssertions = getRequest("/leave");
		checkRedirect(statusAssertions, "Location", ".+/login");
	}

	@Test
	void canNotMoveSignUpPageAfterLogin() {
		StatusAssertions statusAssertions = getRequestWithSession("/signup");
		checkRedirect(statusAssertions, "Location", ".+/");
	}

	@Test
	void moveSignUpPageBeforeLogin() {
		getRequest("/signup").isOk();
	}

	@Test
	void canNotMoveLoginPageAfterLogin() {
		StatusAssertions statusAssertions = getRequestWithSession("/login");
		checkRedirect(statusAssertions, "Location", ".+/");
	}

	@Test
	void moveLoginPageBeforeLogin() {
		getRequest("/login").isOk();
	}

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

	private StatusAssertions getRequestWithSession(String requestUri) {
		return webTestClient.get()
				.uri(requestUri)
				.header("Cookie", getCookie())
				.exchange()
				.expectStatus();
	}

	private StatusAssertions getRequest(String requestUri) {
		return webTestClient.get()
				.uri(requestUri)
				.exchange()
				.expectStatus();
	}

	private void checkRedirect(StatusAssertions statusAssertions, String name, String redirectURLRegex) {
		statusAssertions.isFound()
				.expectHeader()
				.valueMatches(name, redirectURLRegex);
	}
}