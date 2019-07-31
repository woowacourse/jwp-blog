package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.UserAuthorizationException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

	public ArticleDto save(Long userId, ArticleDto articleDto) {
		User author = userService.findById(userId);
		return toArticleDto(articleRepository.save(articleDto.toEntity(author)));
	}

	@Transactional
	public void update(long articleId, UserPublicInfoDto loggedInUser, ArticleDto articleDto) {
		Article article = findById(articleId);
		if (article.matchUserId(loggedInUser.getId())) {
			article.updateArticle(articleDto.toVo());
		}
	}

	@Transactional
	public void delete(Long articleId, Long userId) {
		Article article = findById(articleId);
		if (article.matchUserId(userId)) {
			articleRepository.deleteById(articleId);
		}
	}

	public ArticleDto authorize(UserPublicInfoDto userPublicInfoDto, Long articleId) {
		final Article article = findById(articleId);
		if (article.matchUserId(userPublicInfoDto.getId())) {
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
