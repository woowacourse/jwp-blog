package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTests {
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void index() {
		webTestClient.get().uri("/")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void articleForm() {
		webTestClient.get().uri("/writing")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void saveArticle() {
		String title = "제목";
		String contents = "contents";
		String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

		webTestClient.post()
				.uri("/articles")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", title)
						.with("coverUrl", coverUrl)
						.with("contents", contents))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertThat(body.contains(title)).isTrue();
					assertThat(body.contains(coverUrl)).isTrue();
					assertThat(body.contains(contents)).isTrue();
				});
	}

	@Test
	void lookUpArticle() {
		String title = "제목";
		String contents = "contents";
		String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

		articleRepository.save(new Article(title, contents, coverUrl));

		webTestClient.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertThat(body.contains(title)).isTrue();
					assertThat(body.contains(coverUrl)).isTrue();
					assertThat(body.contains(contents)).isTrue();
				});
	}

	@Test
	void findByIndex() {
		String title = "제목";
		String contents = "contents";
		String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

		articleRepository.save(new Article(title, contents, coverUrl));

		webTestClient.get()
				.uri("/article/0")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertThat(body.contains(title)).isTrue();
					assertThat(body.contains(coverUrl)).isTrue();
					assertThat(body.contains(contents)).isTrue();
				});
	}
}
