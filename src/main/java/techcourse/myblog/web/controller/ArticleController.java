package techcourse.myblog.web.controller;

import techcourse.myblog.domain.article.Contents;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.supports.LoginUser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {
	private final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@GetMapping("/writing")
	public String createArticle() {
		return "article-edit";
	}

	@PostMapping("/articles")
	public String saveArticle(@LoginUser User user, Contents contents) {
		Long id = articleService.saveArticle(user, contents);
		return "redirect:/articles/" + id;
	}

	@PutMapping("/articles/{articleId}")
	public String modifyArticle(@LoginUser User user, @PathVariable Long articleId, Contents contents) {
		articleService.update(articleId, user, contents);
		return "redirect:/articles/" + articleId;
	}

	@GetMapping("/articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		model.addAttribute("article", articleService.findById(articleId));
		return "article";
	}

	@GetMapping("/articles/{articleId}/edit")
	public String editArticle(@LoginUser User user, @PathVariable Long articleId, Model model) {
		articleService.confirmAuthorization(user, articleId);
		model.addAttribute("article", articleService.findById(articleId));
		return "article-edit";
	}

	@DeleteMapping("/articles/{articleId}")
	public String deleteArticle(@LoginUser User user, @PathVariable Long articleId) {
		articleService.delete(user, articleId);
		return "redirect:/";
	}
}




