package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.article.Article;
import techcourse.myblog.article.Contents;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.user.User;

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

	public Long saveArticle(String email, Contents contents) {
		User user = userService.findUser(email);
		Article article = contents.valueOfArticle(user);
		return articleRepository.save(article).getId();
	}

	public List<Article> findAll() {
		return articleRepository.findAll();
	}

	@Transactional
	public void update(Long articleId, String email, Contents contents) {
		Article article = findById(articleId);
		confirmAuthorization(email, article.getId());
		article.update(contents.valueOfArticle());
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
