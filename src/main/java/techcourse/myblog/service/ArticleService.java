package techcourse.myblog.service;

import org.springframework.stereotype.Service;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.dto.CommentDto;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.domain.dto.ArticleDto;
import techcourse.myblog.domain.user.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
	private final ArticleRepository articleRepository;

	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public ArticleDto createArticle(ArticleDto articleDto, User author) {
		Article article = articleDto.toEntity(author);
		article = articleRepository.save(article);
		return ArticleDto.toArticleDto(article);
	}

	private Article findArticle(Long articleId) {
		return articleRepository.findById(articleId)
				.orElseThrow(NotFoundObjectException::new);
	}

	public ArticleDto findArticleAndGetDto(Long articleId) {
		return ArticleDto.toArticleDto(findArticle(articleId));
	}

	@Transactional
	public ArticleDto updateArticle(Long articleId, ArticleDto articleDto, User user) {
		Article article = findArticle(articleId);
		article.update(articleDto.toEntity(user));

		return ArticleDto.toArticleDto(article);
	}

	public void deleteArticle(Long articleId, User user) {
		Article article = findArticle(articleId);
		article.checkCorrespondingAuthor(user);
		articleRepository.deleteById(articleId);
	}

	public List<CommentDto> findAllComments(Long articleId) {
		List<Comment> comments = articleRepository.findById(articleId)
				.orElseThrow(NotFoundObjectException::new)
				.getComments();
		return convertCommentsToDto(comments);
	}

	private List<CommentDto> convertCommentsToDto(List<Comment> comments) {
		List<CommentDto> commentDtos = new ArrayList<>();
		comments.forEach(x ->
				commentDtos.add(new CommentDto(x.getId(), x.getAuthor().getUserName(), x.getContents())));
		return commentDtos;
	}

	@Transactional
	public Article addComment(Long articleId, Comment comment) {
		Article article = articleRepository.findById(articleId).orElseThrow(NotFoundObjectException::new);
		article.addComment(comment);
		return article;
	}

	public void checkAvailableUpdateUser(Long articleId, User user) {
		findArticle(articleId).checkCorrespondingAuthor(user);
	}
}
