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

	public Article createArticle(ArticleDto articleDto, User author) {
		Article article = articleDto.toEntity(author);

		return articleRepository.save(article);
	}

	public Article findArticle(Long articleId) {
		return articleRepository.findById(articleId)
				.orElseThrow(NotFoundObjectException::new);
	}

	@Transactional
	public Article updateArticle(Long articleId, Article updatedArticle) {
		Article article = findArticle(articleId);
		article.update(updatedArticle);

		return article;
	}

	public void deleteArticle(Long articleId, User user) {
		Article article = findArticle(articleId);
		article.checkCorrespondingAuthor(user);
		articleRepository.deleteById(articleId);
	}

	public List<CommentDto> findAllComments(Article article) {
		return convertCommentsToDto(article.getComments());
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

	public void checkAvailableUpdateUser(Article article, User user) {
		article.checkCorrespondingAuthor(user);
	}
}
