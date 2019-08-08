package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import techcourse.myblog.domain.user.User;

import java.util.stream.Stream;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class AbstractControllerTest {

	@Autowired
	WebTestClient webTestClient;

	EntityExchangeResult<byte[]> login(User user) {
		return webTestClient.post().uri("/login")
				.body(fromFormData("email", user.getEmail())
						.with("password", user.getPassword()))
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectHeader().valueMatches("Location", ".*/.*")
				.expectBody()
				.returnResult();
	}

	String extractJSessionId(EntityExchangeResult<byte[]> loginResult) {
		String[] cookies = loginResult.getResponseHeaders().get("set-Cookie").stream()
				.filter(it -> it.contains("JSESSIONID"))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("jSessionId가 없습니다."))
				.split(";");
		return Stream.of(cookies)
				.filter(it -> it.contains("JSESSIONID"))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("jSessioId가 없습니다."))
				.split("=")[1];
	}
}
