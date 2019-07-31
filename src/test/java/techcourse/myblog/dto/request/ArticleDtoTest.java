package techcourse.myblog.dto.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleDtoTest {
	private User user;
	private ArticleDto articleDto;
	private Article actualArticle;
	private Article expectedArticle;

	@BeforeEach
	void setUp() {
		articleDto = new ArticleDto("title", "contents", "coverUrl");
		UserDto userDto = new UserDto();
		userDto.setUsername("tiber");
		userDto.setPassword("asdfASDF1@");
		userDto.setEmail("tiber@naver.com");

		user = new User();
		user.saveUser(userDto);
	}

	@Test
	void valueOfArticle() {
		actualArticle = articleDto.valueOfArticle(user);
		expectedArticle = new Article(articleDto, user);

		assertThat(actualArticle.getTitle()).isEqualTo(expectedArticle.getTitle());
		assertThat(actualArticle.getContents()).isEqualTo(expectedArticle.getContents());
		assertThat(actualArticle.getCoverUrl()).isEqualTo(expectedArticle.getCoverUrl());
	}

	@Test
	void valueOfArticleWithArticleId() {
		actualArticle = articleDto.valueOfArticle(1L, user);
		expectedArticle = new Article(1L, articleDto, user);

		assertThat(actualArticle.getTitle()).isEqualTo(expectedArticle.getTitle());
		assertThat(actualArticle.getContents()).isEqualTo(expectedArticle.getContents());
		assertThat(actualArticle.getCoverUrl()).isEqualTo(expectedArticle.getCoverUrl());
	}
}