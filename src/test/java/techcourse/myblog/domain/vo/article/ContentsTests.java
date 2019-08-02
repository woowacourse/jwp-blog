package techcourse.myblog.domain.vo.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.Contents;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;

class ContentsTests {
	private User user;
	private Contents contents;
	private Article actualArticle;
	private Article expectedArticle;

	@BeforeEach
	void setUp() {
		contents = new Contents("title", "contents", "coverUrl");
		UserSignUpInfo userSignUpInfo = new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@");
		user = userSignUpInfo.valueOfUser();
	}

	@Test
	void valueOfArticle() {
		actualArticle = contents.valueOfArticle();
		expectedArticle = new Article(contents);
		confirmArticleFieldValue();
	}

	@Test
	void valueOfArticleWithUser() {
		actualArticle = contents.valueOfArticle(user);
		expectedArticle = new Article(contents, user);
		confirmArticleFieldValue();
	}

	@Test
	void valueOfArticleWithArticleId() {
		actualArticle = contents.valueOfArticle(1L, user);
		expectedArticle = new Article(1L, contents, user);
		confirmArticleFieldValue();
	}

	private void confirmArticleFieldValue() {
		assertThat(actualArticle.getTitle()).isEqualTo(expectedArticle.getTitle());
		assertThat(actualArticle.getText()).isEqualTo(expectedArticle.getText());
		assertThat(actualArticle.getCoverUrl()).isEqualTo(expectedArticle.getCoverUrl());
	}
}
