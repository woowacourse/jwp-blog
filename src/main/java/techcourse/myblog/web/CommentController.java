package techcourse.myblog.web;

import java.util.Optional;
import javax.servlet.http.HttpSession;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.CommentDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

	@PostMapping("/comments/{id}")
	public String saveArticle(CommentDto commentDto, @PathVariable long id, HttpSession session) {
		Object email = session.getAttribute("email");
		Optional<Article> article = articleRepository.findById(id);

		if(article.isPresent() && email != null) {
			User user =  userService.findUser((String) email);
			commentRepository.save(commentDto.valueOf(user, article.get()));
		}

		return "redirect:/articles/" + id;
	}
}
