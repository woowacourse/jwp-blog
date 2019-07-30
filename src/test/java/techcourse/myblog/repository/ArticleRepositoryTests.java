package techcourse.myblog.repository;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.ArticleDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ArticleRepositoryTests {
	private User user;

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private UserRepository userRepository;

	@Test
	void findById() {
		User user = userRepository.findById(1L).get();
		ArticleDto articleDto = new ArticleDto("title", "contents", "www.coverUrl.com");
		Article article = articleDto.valueOfArticle(user);

		article = testEntityManager.persist(article);
		assertThat(article.getAuthor().getId()).isEqualTo(user.getId());
	}
}
