package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.Contents;
import techcourse.myblog.domain.user.User;
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

	public Long saveArticle(User loginUser, Contents contents) {
		User user = userService.findUser(loginUser);
		Article article = new Article(user, contents);
		return articleRepository.save(article).getId();
	}

	public List<Article> findAll() {
		return articleRepository.findAll();
	}

	@Transactional
	public void update(Long articleId, User loginUser, Contents contents) {
		Article article = findById(articleId);
		confirmAuthorization(loginUser, article.getId());
		article.update(contents);
	}

	public void confirmAuthorization(User user, Long articleId) {
		Article article = findById(articleId);
		if (!userService.findUser(user).matchUser(article.getAuthor())) {
			throw new UnauthorizedException();
		}
	}

	public Article findById(Long articleId) {
		return articleRepository.findById(articleId)
				.orElseThrow(NotFoundArticleException::new);
	}

	@Transactional
	public void delete(User loginUser, Long articleId) {
		confirmAuthorization(loginUser, articleId);
		articleRepository.deleteById(articleId);
	}
}
