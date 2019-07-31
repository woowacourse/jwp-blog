package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
	private static final String LOGGED_IN_USER = "loggedInUser";

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/comment")
	public String createComment(CommentRequestDto commentRequestDto, HttpSession session) {
		commentService.save(getLoggedInUser(session), commentRequestDto);
		return "redirect:/articles/" + commentRequestDto.getArticleId();
	}

	@PutMapping("/articles/{articleId}/comment/{commentId}")
	public String updateComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId,
	                            CommentRequestDto commentRequestDto, HttpSession session) {
		commentService.update(getLoggedInUser(session), commentId, commentRequestDto);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("/articles/{articleId}/comment/{commentId}")
	public String deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId,
	                            HttpSession session) {
		commentService.delete(getLoggedInUser(session), commentId);
		return "redirect:/articles/" + articleId;
	}

	private UserPublicInfoDto getLoggedInUser(HttpSession session) {
		UserPublicInfoDto user = (UserPublicInfoDto) session.getAttribute(LOGGED_IN_USER);
		if (user == null) {
			throw new NotLoggedInException();
		}
		return user;
	}
}
