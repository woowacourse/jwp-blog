package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.service.ArticleService;

@Controller
public class ArticleController {
	private final ArticleService articleService;

	@Autowired
	public ArticleController(final ArticleService articleService) {
		this.articleService = articleService;
	}

	@GetMapping("/")
	public String index(Model model) {
		final Iterable<Article> articles = articleService.findAll();
		model.addAttribute("articles", articles);
		return "index";
	}

	@GetMapping("/writing")
	public String showWritingPage() {
		return "article/article-edit";
	}

	@PostMapping("/articles")
	public String save(final Article articleParam) {
		Article article = articleService.save(articleParam);
		return "redirect:/articles/" + article.getId();
	}

	@GetMapping("/articles/{articleId}")
	public String findById(@PathVariable final long articleId, final Model model) {
		Article article = articleService.findById(articleId);
		model.addAttribute("article", article);
		return "/article/article";
	}

	@PutMapping("/articles/{articleId}")
	public String update(@PathVariable final long articleId, final Article articleParam) {
		long id = articleService.update(articleParam, articleId);
		return "redirect:/articles/" + id;
	}

	@DeleteMapping("/articles/{articleId}")
	public String delete(@PathVariable final long articleId) {
		articleService.deleteById(articleId);
		return "redirect:/";
	}

	@GetMapping("/articles/{articleId}/edit")
	public String editPage(@PathVariable final long articleId, final Model model) {
		Article article = articleService.findById(articleId);
		model.addAttribute("article", article);
		return "/article/article-edit";
	}
}
