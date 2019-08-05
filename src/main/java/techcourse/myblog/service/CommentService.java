package techcourse.myblog.service;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.Contents;
import techcourse.myblog.domain.user.User;
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

	public Comment save(User loginUser, Long articleId, Contents contents) {
		User user = userService.findUser(loginUser);
		Article article = articleService.findById(articleId);
		Comment comment = new Comment(user, contents);
		article.addComment(comment);
		return commentRepository.save(comment);
	}

	@Transactional
	public void update(User loginUser, Long articleId, Long commentId, Contents contents) {
		Comment comment = findById(commentId);
		existArticle(articleId);
		confirmAuthorization(loginUser, comment.getAuthor());
		comment.update(contents);
	}

	public Comment findById(Long commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(NotFoundCommentException::new);
	}

	private void existArticle(Long articleId) {
		articleService.findById(articleId);
	}

	private void confirmAuthorization(User loginUser, User commentAuthor) {
		User user = userService.findUser(loginUser);
		if (!user.matchUser(commentAuthor)) {
			throw new UnauthorizedException();
		}
	}

	@Transactional
	public void delete(User loginUser, Long articleId, Long commentId) {
		Comment comment = findById(commentId);
		confirmAuthorization(loginUser, comment.getAuthor());
		existArticle(articleId);
		commentRepository.deleteById(commentId);
	}
}
