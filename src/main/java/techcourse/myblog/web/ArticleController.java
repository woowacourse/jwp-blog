package techcourse.myblog.web;

import javax.servlet.http.HttpSession;

import techcourse.myblog.dto.request.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {
	private final ArticleService articleService;
	private final CommentService commentService;

	public ArticleController(ArticleService articleService, CommentService commentService) {
		this.articleService = articleService;
		this.commentService = commentService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("articles", articleService.findAll());
		return "index";
	}

	@GetMapping("/writing")
	public String createArticle(HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		return "article-edit";
	}

	@PostMapping("/articles")
	public String saveArticle(ArticleDto articleDto, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		String email = httpSession.getAttribute("email").toString();
		Long id = articleService.saveArticle(email, articleDto);
		return "redirect:/articles/" + id;
	}

	@PutMapping("/articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, ArticleDto articleDto, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		String email = httpSession.getAttribute("email").toString();
		articleService.update(articleId, email, articleDto);
		return "redirect:/articles/" + articleId;
	}

	@GetMapping("/articles/{articleId}")
	public String getArticle(@PathVariable long articleId, Model model) {
		model.addAttribute("article", articleService.findById(articleId));
		model.addAttribute("comments", commentService.findByArticleId(articleId));
		return "article";
	}

	@GetMapping("/articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		String email = httpSession.getAttribute("email").toString();
		articleService.confirmAuthorization(email, articleId);
		model.addAttribute("article", articleService.findById(articleId));
		return "article-edit";
	}

	@DeleteMapping("/articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		String email = httpSession.getAttribute("email").toString();
		articleService.confirmAuthorization(email, articleId);
		commentService.deleteByArticleId(articleId);
		articleService.delete(articleId);
		return "redirect:/";
	}

	private boolean existSession(HttpSession httpSession) {
		return (httpSession.getAttribute("email") != null);
	}
}




