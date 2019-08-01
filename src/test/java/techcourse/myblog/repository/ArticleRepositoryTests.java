package techcourse.myblog.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.ArticleDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ArticleRepositoryTests {
	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Test
	void findById() {
		User user = userRepository.findById(1L).get();
		ArticleDto articleDto = new ArticleDto("title", "contents", "www.coverUrl.com");
		Article actualArticle = articleDto.valueOfArticle(user);
		actualArticle = testEntityManager.persist(actualArticle);
		Article expectArticle = articleRepository.findById(actualArticle.getId()).get();
		assertThat(actualArticle.matchArticle(expectArticle));
	}

	@Test
	void deleteArticleWithComment() {
		Article article = articleRepository.findById(2L).get();
		List<Comment> comments = article.getComments();
		assertTrue(comments.size() == 2);
		articleRepository.deleteById(2L);
		assertThat(commentRepository.findById(comments.get(0).getId())).isEmpty();
		assertThat(commentRepository.findById(comments.get(1).getId())).isEmpty();
		assertThat(userRepository.findById(1L)).isNotEmpty();
	}
}
