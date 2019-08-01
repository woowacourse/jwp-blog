package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.vo.article.ArticleVo;
import techcourse.myblog.domain.vo.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleVoTests {
	private User user;
	private ArticleVo articleVo;
	private Article actualArticle;
	private Article expectedArticle;

	@BeforeEach
	void setUp() {
		articleVo = new ArticleVo("title", "contents", "coverUrl");
		UserSignUpInfo userSignUpInfo = new UserSignUpInfo("tiber", "asdfASDF1@", "tiber@naver.com");
		user = userSignUpInfo.valueOfUser();
	}

	@Test
	void valueOfArticle() {
		actualArticle = articleVo.valueOfArticle();
		expectedArticle = new Article(articleVo);
		confirmArticleFieldValue();
	}

	@Test
	void valueOfArticleWithUser() {
		actualArticle = articleVo.valueOfArticle(user);
		expectedArticle = new Article(articleVo, user);
		confirmArticleFieldValue();
	}

	@Test
	void valueOfArticleWithArticleId() {
		actualArticle = articleVo.valueOfArticle(1L, user);
		expectedArticle = new Article(1L, articleVo, user);
		confirmArticleFieldValue();
	}

	private void confirmArticleFieldValue() {
		assertThat(actualArticle.getTitle()).isEqualTo(expectedArticle.getTitle());
		assertThat(actualArticle.getContents()).isEqualTo(expectedArticle.getContents());
		assertThat(actualArticle.getCoverUrl()).isEqualTo(expectedArticle.getCoverUrl());
	}
}
