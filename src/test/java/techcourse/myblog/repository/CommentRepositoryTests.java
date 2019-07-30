package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.ArticleDto;
import techcourse.myblog.dto.request.CommentDto;
import techcourse.myblog.dto.request.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTests {
	private User user;
	private Article article;

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	void setUp() {
		UserDto userDto = new UserDto();
		userDto.setUsername("tiber");
		userDto.setPassword("asdfASDF1@");
		userDto.setEmail("tiber@naver.com");

		user = new User();
		user.saveUser(userDto);

		user = testEntityManager.persist(user);
		testEntityManager.flush();
		testEntityManager.clear();

		ArticleDto articleDto = new ArticleDto("title", "contents", "www.coverUrl.com");
		article = articleDto.valueOfArticle(user);
		article = testEntityManager.persist(article);
	}

	@Test
	void findById() {
		CommentDto commentDto = new CommentDto("title");
		Comment actualComment = commentDto.valueOf(user, article);
		actualComment = testEntityManager.persist(actualComment);
		testEntityManager.flush();
		testEntityManager.clear();
		Comment expectComment = commentRepository.findById(actualComment.getId()).get();

		assertThat(actualComment.getAuthor()).isEqualTo(expectComment.getAuthor());
		assertThat(actualComment.getArticle()).isEqualTo(expectComment.getArticle());
		assertThat(actualComment.getContents()).isEqualTo(expectComment.getContents());
	}

	@Test
	void findByArticleId() {
		CommentDto commentDto = new CommentDto("title");
		Comment comment = commentDto.valueOf(user, article);
		testEntityManager.persist(comment);
		testEntityManager.flush();
		testEntityManager.clear();
		assertThat(commentRepository.findByArticleId(article.getId()).size()).isEqualTo(1);
	}

	@Test
	void deleteByArticleId() {
		CommentDto commentDto = new CommentDto("title");
		Comment comment = commentDto.valueOf(user, article);
		Comment comment2 = commentDto.valueOf(user, article);
		testEntityManager.persist(comment);
		testEntityManager.persist(comment2);
		testEntityManager.flush();
		testEntityManager.clear();

		commentRepository.deleteByArticleId(article.getId());
		assertThat(commentRepository.findByArticleId(article.getId()).size()).isEqualTo(0);
	}
}