package techcourse.myblog.web;

import techcourse.myblog.custom.LoginUser;
import techcourse.myblog.domain.comment.Contents;
import techcourse.myblog.domain.user.User;
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
	public String save(@LoginUser User user, Contents contents, @PathVariable Long articleId) {
		commentService.save(user, articleId, contents);
		return "redirect:/articles/" + articleId;
	}

	@PutMapping("/comments/{articleId}/{commentId}")
	public String update(@LoginUser User user, Contents contents, @PathVariable Long articleId, @PathVariable Long commentId) {
		commentService.update(user, articleId, commentId, contents);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("/comments/{articleId}/{commentId}")
	public String delete(@LoginUser User user, @PathVariable Long articleId, @PathVariable Long commentId) {
		commentService.delete(user, articleId, commentId);
		return "redirect:/articles/" + articleId;
	}
}
