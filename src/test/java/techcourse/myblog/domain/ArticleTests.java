package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.vo.article.ArticleContents;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTests {
	@Test
	void update() {
		ArticleContents articleContents = new ArticleContents("title", "contents", "www.coverUrl.com");
		Article article = articleContents.valueOfArticle();
		Article modifiedArticle = articleContents.valueOfArticle();
		article.update(modifiedArticle);
		assertThat(article.getText()).isEqualTo(modifiedArticle.getText());
		assertThat(article.getContents()).isEqualTo(modifiedArticle.getContents());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}
}