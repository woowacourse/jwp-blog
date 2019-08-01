package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleVo;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.repository.ArticleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {
	private ArticleRepository articleRepository;
	private UserService userService;

	public ArticleService(ArticleRepository articleRepository, UserService userService) {
		this.articleRepository = articleRepository;
		this.userService = userService;
	}

	public Long saveArticle(String email, ArticleVo articleVo) {
		User user = userService.findUser(email);
		Article article = articleVo.valueOfArticle(user);
		return articleRepository.save(article).getId();
	}

	public List<Article> findAll() {
		return articleRepository.findAll();
	}

	@Transactional
	public void update(Long articleId, String email, ArticleVo articleVo) {
		Article article = findById(articleId);
		confirmAuthorization(email, article.getId());
		article.update(articleVo.valueOfArticle());
	}

	public void confirmAuthorization(String email, Long articleId) {
		Article article = findById(articleId);
		if (!userService.findUser(email).matchUser(article.getAuthor())) {
			throw new UnauthorizedException();
		}
	}

	public Article findById(Long articleId) {
		return articleRepository.findById(articleId)
				.orElseThrow(NotFoundArticleException::new);
	}

	@Transactional
	public void delete(Long articleId) {
		articleRepository.deleteById(articleId);
	}
}
