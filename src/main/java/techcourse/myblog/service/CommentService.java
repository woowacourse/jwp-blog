package techcourse.myblog.service;

import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.Contents;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.user.User;

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

	public Comment save(String email, Long articleId, Contents contents) {
		User user = userService.findUser(email);
		Article article = articleService.findById(articleId);
		Comment comment = contents.valueOf(user);
		article.addComment(comment);
		return commentRepository.save(comment);
	}

	@Transactional
	public void update(String email, Long articleId, Long commentId, Contents contents) {
		Comment comment = findById(commentId);
		existArticle(articleId);
		confirmAuthorization(email, comment.getAuthor());
		comment.update(contents.valueOf());
	}

	public Comment findById(Long commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(NotFoundCommentException::new);
	}

	private void existArticle(Long articleId) {
		articleService.findById(articleId);
	}

	private void confirmAuthorization(String email, User commentAuthor) {
		User user = userService.findUser(email);
		if (!user.matchUser(commentAuthor)) {
			throw new UnauthorizedException();
		}
	}

	@Transactional
	public void delete(String email, Long articleId, Long commentId) {
		Comment comment = findById(commentId);
		confirmAuthorization(email, comment.getAuthor());
		existArticle(articleId);
		commentRepository.deleteById(commentId);
	}
}
