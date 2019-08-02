package techcourse.myblog.web;

import javax.servlet.http.HttpSession;

import techcourse.myblog.comment.Contents;
import techcourse.myblog.service.CommentService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class CommentController {
	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/comments/{articleId}")
	public String save(Contents contents, @PathVariable Long articleId, HttpSession session) {
		String email = session.getAttribute("email").toString();
		commentService.save(email, articleId, contents);
		return "redirect:/articles/" + articleId;
	}

	@PutMapping("/comments/{articleId}/{commentId}")
	public String update(Contents contents, @PathVariable Long articleId, @PathVariable Long commentId, HttpSession session) {
		String email = session.getAttribute("email").toString();
		commentService.update(email, articleId, commentId, contents);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("/comments/{articleId}/{commentId}")
	public String delete(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession session) {
		String email = session.getAttribute("email").toString();
		commentService.delete(email, articleId, commentId);
		return "redirect:/articles/" + articleId;
	}
}
