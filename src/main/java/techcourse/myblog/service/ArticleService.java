package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
	@Autowired
	private ArticleRepository articleRepository;

	public Article save(Article article) {
		return articleRepository.save(article);
	}

	public List<Article> findAll() {
		return articleRepository.findAll();
	}

	public Article findById(long id) {
		return articleRepository.findById(id);
	}

	public void update(Article article) {
		articleRepository.update(article);
	}

	public void deleteById(int articleId) {
		articleRepository.deleteById(articleId);
	}
}
