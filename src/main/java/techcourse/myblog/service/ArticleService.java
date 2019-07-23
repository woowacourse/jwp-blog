package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	@Transactional(readOnly = true)
	public Article findById(final long id) {
		return articleRepository.findById(id)
				.orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	public long update(final long id, final Article articleParam) {
		Article article = findById(id);
		article.update(articleParam);
		return article.getId();
	}

	public void deleteById(final long id) {
		articleRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Iterable<Article> findAll() {
		return articleRepository.findAll();
	}

	public void deleteAll() {
		articleRepository.deleteAll();
	}
}
