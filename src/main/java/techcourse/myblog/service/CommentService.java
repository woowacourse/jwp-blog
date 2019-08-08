package techcourse.myblog.service;

import org.springframework.stereotype.Service;

import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.domain.dto.CommentDto;
import techcourse.myblog.domain.user.User;

import javax.transaction.Transactional;

@Service
public class CommentService {
	private UserService userService;
	private CommentRepository commentRepository;

	public CommentService(UserService userService, CommentRepository commentRepository) {
		this.userService = userService;
		this.commentRepository = commentRepository;
	}

	public Comment createComment(CommentDto commentDto, LoginUser loginUser) {
		User user = userService.findByEmail(loginUser.getEmail());
		Comment comment = commentDto.toEntity(user);
		return commentRepository.save(comment);
	}

	public void deleteComment(Long commentId, LoginUser loginUser) {
		User user = userService.findByEmail(loginUser.getEmail());
		Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundObjectException::new);
		comment.checkCorrespondingAuthor(user);
		commentRepository.deleteById(commentId);
	}

	@Transactional
	public Comment updateComment(Long commentId, LoginUser loginUser, CommentDto commentDto) {
		User user = userService.findByEmail(loginUser.getEmail());
		Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundObjectException::new);
		Comment updateComment = commentDto.toEntity(user);
		comment.update(updateComment, user);
		return comment;
	}
}
