package techcourse.myblog.web;

import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {
	@Autowired
	private ArticleService articleService;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("articles", articleService.findAll());
		return "index";
	}

	@GetMapping("writing")
	public String createArticle() {
		return "article-edit";
	}

	@PostMapping("articles")
	public String saveArticle(Article article, Model model) {
		model.addAttribute(article);
		articleService.save(article);
		return "article";
	}

	@GetMapping("articles/{articleId}")
	public String getArticle(@PathVariable int articleId, Model model) {
		model.addAttribute("article", articleService.findById(articleId));
		return "article";
	}

	@GetMapping("articles/{articleId}/edit")
	public String editArticle(@PathVariable int articleId, Model model) {
		model.addAttribute("article", articleService.findById(articleId));
		return "article-edit";
	}

	@PutMapping("articles/{articleId}")
	public String modifyArticle(@PathVariable int articleId, Article article, Model model) {
		article.setId(articleId);
		articleService.update(article);
		model.addAttribute(articleService.findById(articleId));
		return "article";
	}

	@DeleteMapping("articles/{articleId}")
	public String deleteArticle(@PathVariable int articleId) {
		articleService.deleteById(articleId);
		return "redirect:/";
	}
}




