package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.Contents;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTests {
	@Test
	void update() {
		Contents contents = new Contents("title", "contents", "www.coverUrl.com");
		Article article = contents.valueOfArticle();
		Article modifiedArticle = contents.valueOfArticle();
		article.update(modifiedArticle);
		assertThat(article.getTitle()).isEqualTo(modifiedArticle.getTitle());
		assertThat(article.getText()).isEqualTo(modifiedArticle.getText());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}
}