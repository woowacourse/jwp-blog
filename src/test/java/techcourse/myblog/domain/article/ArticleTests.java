package techcourse.myblog.domain.article;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.Information;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.request.UserSignUpInfoDto;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTests {
	@Test
	void update() {
		Information userInfo = new UserSignUpInfoDto("tiber", "tiber@naver.com", "asdfASDF1@")
				.valueOfInfo();
		User user = new User(userInfo);

		Contents actualContents = new Contents("title", "contents", "www.coverUrl.com");
		Article article = new Article(user, actualContents);
		Contents updateContents = new Contents("updateTitle", "updateContents", "www.updateUrl.com");
		Article modifiedArticle = new Article(user, updateContents);

		article.update(updateContents);

		assertThat(article.getTitle()).isEqualTo(modifiedArticle.getTitle());
		assertThat(article.getText()).isEqualTo(modifiedArticle.getText());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}
}