package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTests {
	@Test
	void update() {
		ArticleVo articleVo = new ArticleVo("title", "contents", "www.coverUrl.com");
		Article article = articleVo.valueOfArticle();
		Article modifiedArticle = articleVo.valueOfArticle();
		article.update(modifiedArticle);
		assertThat(article.getTitle()).isEqualTo(modifiedArticle.getTitle());
		assertThat(article.getContents()).isEqualTo(modifiedArticle.getContents());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}
}