package techcourse.myblog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.CommentDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CommentRepositoryTests {
	private User user;
	private Article article;
	private Comment actualComment;

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	void setUp() {
		user = userRepository.findById(1L).get();
		article = articleRepository.findById(1L).get();

		CommentDto commentDto = new CommentDto("title");
		actualComment = commentDto.valueOf(user, article);
		actualComment = testEntityManager.persist(actualComment);
		testEntityManager.flush();
		testEntityManager.clear();
	}

	@Test
	void findById() {
		Comment expectComment = commentRepository.findById(actualComment.getId()).get();
		assertTrue(actualComment.getAuthor().matchUser(expectComment.getAuthor()));
		assertTrue(actualComment.matchComment(expectComment));
	}

	@Test
	void deleteByArticleId() {
		commentRepository.deleteByArticleId(article.getId());
		assertThat(commentRepository.findByArticleId(article.getId()).size()).isEqualTo(0);
	}
}