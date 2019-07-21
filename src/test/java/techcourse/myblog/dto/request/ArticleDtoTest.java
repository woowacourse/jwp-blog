package techcourse.myblog.dto.request;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleDtoTest {
	@Test
	void valueOfArticle() {
		ArticleDto articleDto = new ArticleDto(1L, "title", "contents", "coverUrl");
		Article actualArticle = articleDto.valueOfArticle();
		Article expectedArticle = new Article(1L, "title", "contents", "coverUrl");
		assertThat(actualArticle).isEqualTo(expectedArticle);
	}
}