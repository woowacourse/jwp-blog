package techcourse.myblog.web;

import javax.servlet.http.HttpSession;

import techcourse.myblog.dto.request.ArticleDto;
import techcourse.myblog.service.ArticleService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {
	private final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("articles", articleService.findAll());
		return "index";
	}

	@GetMapping("/writing")
	public String createArticle() {
		return "article-edit";
	}

	@PostMapping("/articles")
	public String saveArticle(ArticleDto articleDto, HttpSession httpSession) {
		String email = httpSession.getAttribute("email").toString();
		Long id = articleService.saveArticle(email, articleDto);
		return "redirect:/articles/" + id;
	}

	@PutMapping("/articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, ArticleDto articleDto, HttpSession httpSession) {
		String email = httpSession.getAttribute("email").toString();
		articleService.update(articleId, email, articleDto);
		return "redirect:/articles/" + articleId;
	}

	@GetMapping("/articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		model.addAttribute("article", articleService.findById(articleId));
		return "article";
	}

	@GetMapping("/articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model, HttpSession httpSession) {
		String email = httpSession.getAttribute("email").toString();
		articleService.confirmAuthorization(email, articleId);
		model.addAttribute("article", articleService.findById(articleId));
		return "article-edit";
	}

	@DeleteMapping("/articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
		String email = httpSession.getAttribute("email").toString();
		articleService.confirmAuthorization(email, articleId);
		articleService.delete(articleId);
		return "redirect:/";
	}
}




