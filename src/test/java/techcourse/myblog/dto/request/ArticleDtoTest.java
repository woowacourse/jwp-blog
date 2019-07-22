package techcourse.myblog.dto.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleDtoTest {
	private ArticleDto articleDto;
	private Article actualArticle;
	private Article expectedArticle;

	@BeforeEach
	void setUp() {
		articleDto = new ArticleDto("title", "contents", "coverUrl");
	}

	@Test
	void valueOfArticle() {
		actualArticle = articleDto.valueOfArticle();
		expectedArticle = new Article("title", "contents", "coverUrl");
		assertThat(actualArticle).isEqualTo(expectedArticle);
	}

	@Test
	void valueOfArticleWithArticleId() {
		actualArticle = articleDto.valueOfArticle(1L);
		expectedArticle = new Article(1L, "title", "contents", "coverUrl");
		assertThat(actualArticle).isEqualTo(expectedArticle);
	}
}