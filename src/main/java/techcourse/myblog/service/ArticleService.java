package techcourse.myblog.service;

import org.springframework.stereotype.Service;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.dto.response.ArticleResponseDto;
import techcourse.myblog.domain.dto.response.CommentResponseDto;
import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.domain.dto.ArticleDto;
import techcourse.myblog.domain.user.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
	private final ArticleRepository articleRepository;
	private final UserService userService;

	public ArticleService(ArticleRepository articleRepository, UserService userService) {
		this.articleRepository = articleRepository;
		this.userService = userService;
	}

	public ArticleResponseDto createArticle(ArticleDto articleDto, LoginUser loginUser) {
		User user = userService.findByEmail(loginUser.getEmail());
		Article article = articleDto.toEntity(user);
		article = articleRepository.save(article);
		return ArticleResponseDto.toArticleResponseDto(article);
	}

	private Article findArticle(Long articleId) {
		return articleRepository.findById(articleId)
				.orElseThrow(NotFoundObjectException::new);
	}

	public ArticleResponseDto findArticleAndGetDto(Long articleId) {
		return ArticleResponseDto.toArticleResponseDto(findArticle(articleId));
	}

	@Transactional
	public ArticleResponseDto updateArticle(Long articleId, ArticleDto articleDto, LoginUser loginUser) {
		User user = userService.findByEmail(loginUser.getEmail());
		Article article = findArticle(articleId);
		article.update(articleDto.toEntity(user));

		return ArticleResponseDto.toArticleResponseDto(article);
	}

	public void deleteArticle(Long articleId, LoginUser loginUser) {
		Article article = findArticle(articleId);
		article.checkCorrespondingAuthor(loginUser.getEmail());
		articleRepository.deleteById(articleId);
	}

	public List<CommentResponseDto> findAllComments(Long articleId) {
		List<Comment> comments = articleRepository.findById(articleId)
				.orElseThrow(NotFoundObjectException::new)
				.getComments();
		return convertCommentsToDto(comments);
	}

	private List<CommentResponseDto> convertCommentsToDto(List<Comment> comments) {
		List<CommentResponseDto> commentDtos = new ArrayList<>();
		comments.forEach(x ->
				commentDtos.add(new CommentResponseDto(x.getId(), x.getAuthor().getUserName(), x.getContents())));
		return commentDtos;
	}

	@Transactional
	public Article addComment(Long articleId, Comment comment) {
		Article article = articleRepository.findById(articleId).orElseThrow(NotFoundObjectException::new);
		article.addComment(comment);
		return article;
	}

	public void checkAvailableUpdateUser(Long articleId, String email) {
		findArticle(articleId).checkCorrespondingAuthor(email);
	}
}
