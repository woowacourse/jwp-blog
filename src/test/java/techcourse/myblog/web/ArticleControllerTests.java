package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.repository.ArticleRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
	private String title = "제목";
	private String contents = "contents";
	private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";
	private User user = new User();

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ArticleRepository articleRepository;

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

	@Test
	void saveArticle() {
		StatusAssertions statusAssertions = articlePutOrPostRequestWithSession(HttpMethod.POST, "/articles", title, coverUrl, contents);
		checkRedirect(statusAssertions, "Location", ".+/articles/[1-9][0-9]*");
	}

	@Test
	void canNotSaveArticle() {
		StatusAssertions statusAssertions = articlePutOrPostRequest(HttpMethod.POST, "/articles", title, coverUrl, contents);
		checkRedirect(statusAssertions, "Location", ".+/");
	}

	@Test
	void moveIndexPage() {
		requestWithSession(HttpMethod.GET, "/").isOk();
	}

	@Test
	void moveArticleWritingForm() {
		requestWithSession(HttpMethod.GET, "/writing").isOk();
	}

	@Test
	void canNotMoveArticleWritingForm() {
		StatusAssertions statusAssertions = request(HttpMethod.GET, "/writing");
		checkRedirect(statusAssertions, "Location", ".+/");
	}

	@Test
	void findByIndex() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
		requestWithSession(HttpMethod.GET, "/articles" + articleId).isOk();
	}

	@Test
	void deleteArticle() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
		StatusAssertions statusAssertions = requestWithSession(HttpMethod.DELETE, "/articles/" + articleId);
		checkRedirect(statusAssertions, "Location", ".+/");
		assertThat(articleRepository.findById(articleId)).isEmpty();
	}

	@Test
	void canNotDeleteArticle() {
		StatusAssertions statusAssertions = request(HttpMethod.DELETE, "/articles/1");
		checkRedirect(statusAssertions, "Location", ".+/");
	}

	@Test
	void notFoundArticle() {
		requestWithSession(HttpMethod.GET, "/articles/100").isBadRequest();
	}

	@Test
	void notFoundArticleEdit() {
		requestWithSession(HttpMethod.GET, "/articles/100/edit").isBadRequest();
	}

	private StatusAssertions request(HttpMethod httpMethod, String requestURI) {
		return webTestClient
				.method(httpMethod)
				.uri(requestURI)
				.exchange()
				.expectStatus();
	}

	private StatusAssertions requestWithSession(HttpMethod httpMethod, String requestURI) {
		return webTestClient
				.method(httpMethod)
				.uri(requestURI)
				.header("Cookie", getCookie())
				.exchange()
				.expectStatus();
	}

	@Test
	void updateArticle() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
		StatusAssertions statusAssertions = articlePutOrPostRequestWithSession(HttpMethod.PUT, "/articles/" + articleId, "updatedTitle", "updatedCoverUrl", "updatedContents");
		checkRedirect(statusAssertions, "Location", ".+/articles/[1-9][0-9]*");
	}

	@Test
	void canNotUpdateArticle() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();
		StatusAssertions statusAssertions = articlePutOrPostRequest(HttpMethod.PUT, "/articles/" + articleId, "updatedTitle", "updatedCoverUrl", "updatedContents");
		checkRedirect(statusAssertions, "Location", ".+/");
	}

	private void checkRedirect(StatusAssertions statusAssertions, String name, String redirectURLRegex) {
		statusAssertions.isFound()
				.expectHeader()
				.valueMatches(name, redirectURLRegex);
	}

	private StatusAssertions articlePutOrPostRequestWithSession(HttpMethod httpMethod, String requestURI, String title, String coverURL, String contents) {
		return webTestClient
				.method(httpMethod)
				.uri(requestURI)
				.header("Cookie", getCookie())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", title)
						.with("coverUrl", coverURL)
						.with("contents", contents))
				.exchange()
				.expectStatus();
	}

	private StatusAssertions articlePutOrPostRequest(HttpMethod httpMethod, String requestURI, String title, String coverURL, String contents) {
		return webTestClient
				.method(httpMethod)
				.uri(requestURI)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", title)
						.with("coverUrl", coverURL)
						.with("contents", contents))
				.exchange()
				.expectStatus();
	}


}
