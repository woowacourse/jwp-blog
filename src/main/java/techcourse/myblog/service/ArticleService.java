package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.UserAuthorizationException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private ArticleRepository articleRepository;
    private UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
	this.articleRepository = articleRepository;
	this.userService = userService;
    }

    Article findById(Long id) {
	return articleRepository.findById(id)
		.orElseThrow(NotFoundArticleException::new);
    }

    public ArticleDto findArticleDtoById(Long id) {
	return toArticleDto(findById(id));
    }

    public List<ArticleDto> findAll() {
	return articleRepository.findAll().stream()
		.map(this::toArticleDto)
		.collect(Collectors.toList());
    }

    public ArticleDto save(UserSessionDto userSession, ArticleDto articleDto) {
	User author = userService.findById(userSession.getId());
	return toArticleDto(articleRepository.save(articleDto.toEntity(author)));
    }

    public Article update(long articleId, UserSessionDto userSession, ArticleDto articleDto) {
	Article article = findById(articleId);
	User author = userService.findByUserSession(userSession);
	article.updateArticle(articleDto.toEntity(author));
	return article;
    }

    public void delete(Long articleId, UserSessionDto userSession) {
	Article article = findById(articleId);
	User author = userService.findByUserSession(userSession);
	if (article.matchAuthor(author)) {
	    articleRepository.deleteById(articleId);
	}
    }

    public ArticleDto authorize(UserSessionDto userSession, Long articleId) {
	Article article = findById(articleId);
	User author = userService.findByUserSession(userSession);
	if (article.matchAuthor(author)) {
	    return toArticleDto(article);
	}
	throw new UserAuthorizationException();
    }

    private ArticleDto toArticleDto(Article article) {
	return new ArticleDto(article.getId(),
		article.getAuthorId(),
		article.getTitle(),
		article.getCoverUrl(),
		article.getContents());
    }
}
