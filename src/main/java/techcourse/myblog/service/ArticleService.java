package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;

import java.util.NoSuchElementException;

@Service
public class ArticleService {

	private final ArticleRepository articleRepository;

	@Autowired
	public ArticleService(final ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}


	public Article save(final Article article) {
		return articleRepository.save(article);
	}

	public Article findById(final long id) {
		return articleRepository.findById(id)
				.orElseThrow(NoSuchElementException::new);
	}

	public long update(final Article articleParam, final long id) {
		Article article = articleRepository.findById(id)
				.orElseThrow(NoSuchElementException::new);
		article.update(articleParam);
		return articleRepository.save(article).getId();
	}

	public void deleteById(final long id) {
		articleRepository.deleteById(id);
	}

	public Iterable<Article> findAll() {
		return articleRepository.findAll();
	}

	public void deleteAll() {
		articleRepository.deleteAll();
	}
}
