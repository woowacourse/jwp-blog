package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTests {
	@Test
	void update() {
		Article article = new Article("title", "contents", "www.coverUrl.com");
		Article modifiedArticle = new Article("changedTitle", "changedContents", "www.changedCoverUrl.com");
		article.update(modifiedArticle);
		assertThat(article.getTitle()).isEqualTo(modifiedArticle.getTitle());
		assertThat(article.getContents()).isEqualTo(modifiedArticle.getContents());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}
}