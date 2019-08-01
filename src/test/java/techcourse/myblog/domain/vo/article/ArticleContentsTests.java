package techcourse.myblog.domain.vo.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.vo.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleContentsTests {
	private User user;
	private ArticleContents articleContents;
	private Article actualArticle;
	private Article expectedArticle;

	@BeforeEach
	void setUp() {
		articleContents = new ArticleContents("title", "contents", "coverUrl");
		UserSignUpInfo userSignUpInfo = new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@");
		user = userSignUpInfo.valueOfUser();
	}

	@Test
	void valueOfArticle() {
		actualArticle = articleContents.valueOfArticle();
		expectedArticle = new Article(articleContents);
		confirmArticleFieldValue();
	}

	@Test
	void valueOfArticleWithUser() {
		actualArticle = articleContents.valueOfArticle(user);
		expectedArticle = new Article(articleContents, user);
		confirmArticleFieldValue();
	}

	@Test
	void valueOfArticleWithArticleId() {
		actualArticle = articleContents.valueOfArticle(1L, user);
		expectedArticle = new Article(1L, articleContents, user);
		confirmArticleFieldValue();
	}

	private void confirmArticleFieldValue() {
		assertThat(actualArticle.getTitle()).isEqualTo(expectedArticle.getTitle());
		assertThat(actualArticle.getText()).isEqualTo(expectedArticle.getText());
		assertThat(actualArticle.getCoverUrl()).isEqualTo(expectedArticle.getCoverUrl());
	}
}
