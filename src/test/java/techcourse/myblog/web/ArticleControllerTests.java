package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

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
	private String title = "제목";
	private String contents = "contents";
	private String coverUrl = "https://image-notepet.akamaized.net/resize/620x-/seimage/20190222%2F88df4645d7d2a4d2ed42628d30cd83d0.jpg";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void index() {
		webTestClient.get().uri("/")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void articleForm() {
		webTestClient.get().uri("/writing")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void saveArticle() {
		webTestClient.post()
				.uri("/articles")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", title)
						.with("coverUrl", coverUrl)
						.with("contents", contents))
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/articles/[0-9]+");
	}

	@Test
	void updateArticle() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();

		webTestClient.put()
				.uri("/articles/" + articleId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters
						.fromFormData("title", "updatedTitle")
						.with("coverUrl", "updatedCoverUrl")
						.with("contents", "updatedContents"))
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/articles/[1-9]+");
	}

	@Test
	void lookUpArticle() {
		articleRepository.save(new Article(title, contents, coverUrl));

		webTestClient.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void findByIndex() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();

		webTestClient.get()
				.uri("/articles/" + articleId)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void deleteArticle() {
		Long articleId = articleRepository.save(new Article(title, contents, coverUrl)).getId();

		webTestClient.delete()
				.uri("/articles/" + articleId)
				.exchange()
				.expectStatus()
				.isFound()
				.expectHeader()
				.valueMatches("Location", ".+/");

		assertThat(articleRepository.findById(articleId)).isEmpty();
	}
}
