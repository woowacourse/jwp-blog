package techcourse.myblog.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void 메인화면_조회() {
		webTestClient.get().uri("/")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void 글쓰기_폼_열기() {
		webTestClient.get().uri("/articles")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void 게시물_생성() {
		createArticle();
	}

	private EntityExchangeResult<byte[]> createArticle() {
		String title = "제목";
		String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
		String contents = "CONTENTS";

		return webTestClient.post().uri("/articles")
				.body(fromFormData("title", title)
						.with("coverUrl", coverUrl)
						.with("contents", contents))
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectHeader().valueMatches("Location", ".*/articles/[0-9]*")
				.expectBody()
				.returnResult();
	}

	@Test
	void 게시글_수정() {
		String title = "제목";
		String coverUrl = "https://i.pinimg.com/736x/78/28/39/7828390ef4efbe704e480440f3bd3875.jpg";
		String contents = "CONTENTS";

		String updateUrl = createArticle().getResponseHeaders()
				.getLocation().getPath();
		webTestClient.put().uri(updateUrl)
				.body(fromFormData("title", title)
						.with("coverUrl", coverUrl)
						.with("contents", contents))
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectHeader().valueMatches("Location", ".*/articles/[0-9]*");
	}

	@Test
	void 게시글_삭제() {
		String articleId = createArticle().getResponseHeaders()
				.getLocation().getPath().split("/")[2];
		webTestClient.delete().uri("/articles/" + articleId)
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectHeader().valueMatches("Location", ".*/");
	}
}
