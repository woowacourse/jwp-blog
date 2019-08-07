package techcourse.myblog.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
	private static final String SAMPLE_TITLE = "SAMPLE_TITLE";
	private static final String SAMPLE_COVER_URL = "SAMPLE_COVER_URL";
	private static final String SAMPLE_CONTENTS = "SAMPLE_CONTENTS";

	private String baseUrl;
	private String setUpArticleUrl;

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	public void setUp() {
		baseUrl = "http://localhost:" + randomServerPort;

		setUpArticleUrl = given()
				.param("title", SAMPLE_TITLE)
				.param("coverUrl", SAMPLE_COVER_URL)
				.param("contents", SAMPLE_CONTENTS)
				.cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
				.post(baseUrl + "/articles")
				.getHeader("Location");
	}

	@Test
	public void index() {
		webTestClient.get().uri("/")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void showArticles() {
		webTestClient.get().uri("/articles")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void showCreatePageWhenUserLogIn() {
		webTestClient.get().uri("/articles/new")
				.cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void showCreatePageWhenUserLogOut() {
		webTestClient.get().uri("/articles/new")
				.exchange()
				.expectStatus().isFound();
	}

	@Test
	public void createArticleWhenLogin() {
		String newTitle = "New Title";
		String newCoverUrl = "New Cover Url";
		String newContents = "New Contents";

		webTestClient.post()
				.uri("/articles")
				.cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", newTitle)
						.with("coverUrl", newCoverUrl)
						.with("contents", newContents))
				.exchange()
				.expectStatus().isFound()
				.expectBody()
				.consumeWith(response -> {
					URI location = response.getResponseHeaders().getLocation();
					webTestClient.get()
							.uri(location)
							.exchange()
							.expectBody()
							.consumeWith(res -> {
								String body = new String(res.getResponseBody());
								assertThat(body.contains(newTitle)).isTrue();
								assertThat(body.contains(newCoverUrl)).isTrue();
								assertThat(body.contains(newContents)).isTrue();
							});
				});
	}

	@Test
	public void createArticleWhenLogout() {
		String newTitle = "New Title";
		String newCoverUrl = "New Cover Url";
		String newContents = "New Contents";

		webTestClient.post()
				.uri("/articles")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", newTitle)
						.with("coverUrl", newCoverUrl)
						.with("contents", newContents))
				.exchange()
				.expectStatus().isFound()
				.expectHeader().valueMatches("location", ".*/login.*");
	}

	@Test
	public void showArticle() {
		webTestClient.get()
				.uri(setUpArticleUrl)
				.exchange()
				.expectBody()
				.consumeWith(res -> {
					String body = new String(res.getResponseBody());
					assertThat(body.contains(SAMPLE_TITLE)).isTrue();
					assertThat(body.contains(SAMPLE_COVER_URL)).isTrue();
					assertThat(body.contains(SAMPLE_CONTENTS)).isTrue();
				});
	}

	@Test
	public void showEditPageWhenUserMatch() {
		webTestClient.get()
				.uri(setUpArticleUrl + "/edit")
				.cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertThat(body.contains(SAMPLE_TITLE)).isTrue();
					assertThat(body.contains(SAMPLE_COVER_URL)).isTrue();
					assertThat(body.contains(SAMPLE_CONTENTS)).isTrue();
				});
	}

	@Test
	public void showEditPageWhenUserMismatch() {
		webTestClient.get()
				.uri(setUpArticleUrl + "/edit")
				.cookie("JSESSIONID",
						LogInControllerTest.logInAsMismatchUser(webTestClient))
				.exchange()
				.expectStatus().isFound()
				.expectHeader().valueMatches("location", ".*/articles/[0-9]+.*");
	}

	@Test
	public void editArticleWhenUserMatch() {
		String newTitle = "test";
		String newCoverUrl = "newCorverUrl";
		String newContents = "newContents";

		webTestClient.put()
				.uri(setUpArticleUrl)
				.cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", newTitle)
						.with("coverUrl", newCoverUrl)
						.with("contents", newContents))
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectBody()
				.consumeWith(response -> {
					URI location = response.getResponseHeaders().getLocation();
					webTestClient.get()
							.uri(location)
							.exchange()
							.expectBody()
							.consumeWith(res -> {
								String body = new String(res.getResponseBody());
								assertThat(body.contains(newTitle)).isTrue();
								assertThat(body.contains(newCoverUrl)).isTrue();
								assertThat(body.contains(newContents)).isTrue();
							});
				});
	}

	@Test
	public void editArticleWhenUserMismatch() {
		String newTitle = "test";
		String newCoverUrl = "newCorverUrl";
		String newContents = "newContents";

		webTestClient.put()
				.uri(setUpArticleUrl)
				.cookie("JSESSIONID",
						LogInControllerTest.logInAsMismatchUser(webTestClient))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", newTitle)
						.with("coverUrl", newCoverUrl)
						.with("contents", newContents))
				.exchange()
				.expectStatus().isFound()
				.expectHeader().valueMatches("location", ".*/articles/[0-9]+.*");
	}

	@Test
	public void deleteArticleWithMatchUser() {
		webTestClient.delete()
				.uri(setUpArticleUrl)
				.cookie("JSESSIONID", LogInControllerTest.logInAsBaseUser(webTestClient))
				.exchange()
				.expectStatus().isFound();

		webTestClient.get()
				.uri(setUpArticleUrl)
				.exchange()
				.expectStatus().isFound();
	}

	@Test
	public void deleteArticleWithMismatchUser() {
		webTestClient.delete()
				.uri(setUpArticleUrl)
				.cookie("JSESSIONID", LogInControllerTest.logInAsMismatchUser(webTestClient))
				.exchange()
				.expectStatus().isFound();

		webTestClient.get()
				.uri(setUpArticleUrl)
				.exchange()
				.expectStatus().isOk();
	}

	@AfterEach
	public void tearDown() {
		delete(setUpArticleUrl);
	}
}
