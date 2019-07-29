package techcourse.myblog.web;

import java.util.Optional;
import javax.servlet.http.HttpSession;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.CommentDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class CommentController {

	private CommentRepository commentRepository;
	private ArticleRepository articleRepository;
	private UserService userService;

	public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository, UserService userService) {
		this.commentRepository = commentRepository;
		this.articleRepository = articleRepository;
		this.userService = userService;
	}

	@PostMapping("/comments/{articleId}")
	public String save(CommentDto commentDto, @PathVariable long articleId, HttpSession session) {
		Object email = session.getAttribute("email");
		Optional<Article> article = articleRepository.findById(articleId);

		if (article.isPresent() && email != null) {
			User user = userService.findUser((String) email);
			commentRepository.save(commentDto.valueOf(user, article.get()));
		}

		return "redirect:/articles/" + articleId;
	}

	@Transactional
	@PutMapping("/comments/{articleId}/{commentId}")
	public String update(CommentDto commentDto, @PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
		Object email = session.getAttribute("email");

		if (email != null) {
			User user = userService.findUser((String) email);
			Comment comment = commentRepository.findById(commentId).get();
			if (user.equals(comment.getAuthor())) {
				comment.update(commentDto.valueOf());
			}
		}
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("/comments/{articleId}/{commentId}")
	public String delete(@PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
		Object email = session.getAttribute("email");

		if (email != null) {
			User user = userService.findUser((String) email);
			Comment comment = commentRepository.findById(commentId).get();
			if (user.equals(comment.getAuthor())) {
				commentRepository.deleteById(commentId);
			}
		}

		return "redirect:/articles/" + articleId;
	}
}
