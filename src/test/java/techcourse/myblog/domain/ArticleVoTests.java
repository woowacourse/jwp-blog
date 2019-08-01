package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.request.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleVoTests {
	private User user;
	private ArticleVo articleVo;
	private Article actualArticle;
	private Article expectedArticle;

	@BeforeEach
	void setUp() {
		articleVo = new ArticleVo("title", "contents", "coverUrl");
		UserDto userDto = new UserDto();
		userDto.setUsername("tiber");
		userDto.setPassword("asdfASDF1@");
		userDto.setEmail("tiber@naver.com");

		user = new User(userDto);
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
