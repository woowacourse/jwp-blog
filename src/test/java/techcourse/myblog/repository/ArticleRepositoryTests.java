package techcourse.myblog.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.exception.NotFoundArticleException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTests {
	private ArticleRepository articleRepository;
	private Article article;

	@BeforeEach
	void init() {
		articleRepository = new ArticleRepository();
		article = new Article("title", "contents", "www.coverUrl.com");
	}

	@Test
	void save() {
		int articleId = articleRepository.save(article).getId();
		Article savedArticle = articleRepository.findById(articleId);

		assertThat(article.getTitle()).isEqualTo(savedArticle.getTitle());
		assertThat(article.getContents()).isEqualTo(savedArticle.getContents());
		assertThat(article.getCoverUrl()).isEqualTo(savedArticle.getCoverUrl());
	}

	@Test
	void findAll() {
		List<Article> articles = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			articles.add(new Article("title" + i, "contents" + i, "www.coverUrl.com" + i));
		}
		articles.forEach(article -> articleRepository.save(article));

		List<Article> savedArticles = articleRepository.findAll();
		for (int i = 0; i < 3; i++) {
			assertThat(articles.get(i).getTitle()).isEqualTo(savedArticles.get(i).getTitle());
			assertThat(articles.get(i).getContents()).isEqualTo(savedArticles.get(i).getContents());
			assertThat(articles.get(i).getCoverUrl()).isEqualTo(savedArticles.get(i).getCoverUrl());
		}
	}

	@Test
	void findById() {
		int articleId = articleRepository.save(article).getId();
		Article findArticle = articleRepository.findById(articleId);

		assertThat(article.getTitle()).isEqualTo(findArticle.getTitle());
		assertThat(article.getContents()).isEqualTo(findArticle.getContents());
		assertThat(article.getCoverUrl()).isEqualTo(findArticle.getCoverUrl());
	}

	@Test
	void update() {
		Article modifiedArticle = new Article("modifiedTitle", "modifiedContents", "www.modifiedCoverUrl.com");
		int articleId = articleRepository.save(article).getId();
		modifiedArticle.setId(articleId);
		articleRepository.update(modifiedArticle);
		article = articleRepository.findById(articleId);

		assertThat(article.getTitle()).isEqualTo(modifiedArticle.getTitle());
		assertThat(article.getContents()).isEqualTo(modifiedArticle.getContents());
		assertThat(article.getCoverUrl()).isEqualTo(modifiedArticle.getCoverUrl());
	}

	@Test
	void deleteById() {
		int articleId = articleRepository.save(article).getId();
		articleRepository.deleteById(articleId);

		assertThrows(NotFoundArticleException.class, () -> articleRepository.findById(articleId));
	}
}
