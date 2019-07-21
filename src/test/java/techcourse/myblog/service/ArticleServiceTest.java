package techcourse.myblog.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.article.Article;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
	private static final String TITLE = "TEST";
	private static final String COVER_URL = "https://img.com";
	private static final String CONTENTS = "TEST_CONTENTS";

	@Autowired
	private ArticleService articleService;

	@BeforeEach
	public void setUp() {
		articleService.deleteAll();
	}

	@Test
	@DisplayName("Article_추가")
	public void saveTest() {
		Article expected = new Article(TITLE, COVER_URL, CONTENTS);
		Article actual = articleService.save(expected);
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Article_ID_조회")
	public void findByIdTest() {
		Article expected = articleService.save(new Article(TITLE, COVER_URL, CONTENTS));
		Article actual = articleService.findById(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	public void updateByIdTest() {
		Article expected = articleService.save(new Article(TITLE, COVER_URL, CONTENTS));
		expected.setTitle("MODIFY");
		expected.setContents("CHANGE");

		long actual = articleService.update(expected, expected.getId());

		assertEquals(expected.getId(), actual);
	}

	@Test
	@DisplayName("")
	public void deleteByIdTest() {
		Article article = articleService.save(new Article(TITLE, COVER_URL, CONTENTS));
		articleService.deleteById(article.getId());

		assertThrows(NoSuchElementException.class, () -> {
			articleService.findById(article.getId());
		});
	}

	@Test
	@DisplayName("Article_목록_조회")
	public void findAllTest() {
		Article article1 = new Article(TITLE, COVER_URL, CONTENTS);
		Article article2 = new Article(TITLE + 1, COVER_URL + "1", CONTENTS + 1);
		articleService.save(article1);
		articleService.save(article2);

		Iterable<Article> actual = articleService.findAll();
		assertThat(actual, Matchers.contains(article1, article2));
	}

}