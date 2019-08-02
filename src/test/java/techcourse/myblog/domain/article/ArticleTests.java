package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.Contents;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTests {
	@Test
	void update() {
		Contents actualContents = new Contents("title", "contents", "www.coverUrl.com");
		Article article = new Article(actualContents);
		Contents updateContents = new Contents("updateTitle", "updateContents", "www.updateUrl.com");
		Article modifiedArticle = new Article(updateContents);
		article.update(updateContents);
		assertThat(article.getTitle()).isEqualTo(modifiedArticle.getTitle());
		assertThat(article.getText()).isEqualTo(modifiedArticle.getText());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}
}