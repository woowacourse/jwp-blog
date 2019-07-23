package techcourse.myblog.interceptor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationInterceptorTest {
	@Autowired
	private WebTestClient webTestClient;

	private void settingSession() {
		webTestClient = WebTestClient.bindToWebHandler(exchange -> {
			String path = exchange.getRequest().getURI().getPath();
			if ("/mypage".equals(path) || "/mypage/edit".equals(path) || "/leave".equals(path)) {
				return exchange.getSession()
						.doOnNext(webSession ->
								webSession.getAttributes().put("email", "tiber@naver.com"))
						.then();
			}
			return null;
		}).build();
	}

	@Test
	void moveMyPageAfterLogin() {
		settingSession();
		webTestClient.get()
				.uri("/mypage")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void moveMyPageEditAfterLogin() {
		settingSession();
		webTestClient.get()
				.uri("/mypage/edit")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void moveLeaveUserPageAfterLogin() {
		settingSession();
		webTestClient.get()
				.uri("/leave")
				.exchange()
				.expectStatus()
				.isOk();
	}
}