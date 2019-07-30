package techcourse.myblog.service;

import java.util.List;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.CommentDto;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.repository.CommentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
	private CommentRepository commentRepository;
	private UserService userService;
	private ArticleService articleService;

	public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.articleService = articleService;
	}

	@Transactional
	public void deleteByArticleId(Long articleId) {
		commentRepository.deleteByArticleId(articleId);
	}

	public List<Comment> findByArticleId(Long articleId) {
		return commentRepository.findByArticleId(articleId);
	}

	public Comment save(String email, Long articleId, CommentDto commentDto) {
		User user = userService.findUser(email);
		Article article = articleService.findById(articleId);
		Comment comment = commentDto.valueOf(user, article);
		return commentRepository.save(comment);
	}

	@Transactional
	public void update(String email, Long articleId, CommentDto commentDto) {
		Comment comment = findById(commentDto.getId());
		existArticle(articleId);
		confirmAuthorization(email, comment.getAuthor().getId());
		comment.update(commentDto.valueOf());
	}

	public Comment findById(Long commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(NotFoundCommentException::new);
	}

	private void existArticle(Long articleId) {
		articleService.findById(articleId);
	}

	private void confirmAuthorization(String email, Long commentAuthorId) {
		User user = userService.findUser(email);
		if (!user.matchUserId(commentAuthorId)) {
			throw new UnauthorizedException();
		}
	}

	@Transactional
	public void delete(String email, Long articleId, Long commentId) {
		Comment comment = findById(commentId);
		confirmAuthorization(email, comment.getAuthor().getId());
		existArticle(articleId);
		commentRepository.deleteById(commentId);
	}
}
