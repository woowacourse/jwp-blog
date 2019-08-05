package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.UserSessionDto;

@Controller
public class CommentController {
	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/comment")
	public String createComment(CommentRequestDto commentRequestDto, UserSessionDto userSession) {
		commentService.save(userSession, commentRequestDto);
		return "redirect:/articles/" + commentRequestDto.getArticleId();
	}

	@PutMapping("/articles/{articleId}/comment/{commentId}")
	public String updateComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId,
	                            CommentRequestDto commentRequestDto, UserSessionDto userSession) {
		commentService.update(userSession, commentId, commentRequestDto);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("/articles/{articleId}/comment/{commentId}")
	public String deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId,
	                            UserSessionDto userSession) {
		commentService.delete(userSession, commentId);
		return "redirect:/articles/" + articleId;
	}
}
