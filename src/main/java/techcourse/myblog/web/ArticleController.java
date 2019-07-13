package techcourse.myblog.web;

import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {
	private final ArticleRepository articleRepository;

	public ArticleController(final ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("articles", articleRepository.findAll());
		return "index";
	}

	@GetMapping("writing")
	public String createArticle() {
		return "article-edit";
	}

	@PostMapping("articles")
	public String saveArticle(Article article, Model model) {
		model.addAttribute(article);
		articleRepository.save(article);
		return "article";
	}

	@GetMapping("articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		model.addAttribute("article", articleRepository.findById(articleId));
		return "article";
	}

	@GetMapping("articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model) {
		model.addAttribute("article", articleRepository.findById(articleId));
		return "article-edit";
	}

	@PutMapping("articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, Article article, Model model) {
		article.setId(articleId);
		articleRepository.update(article);
		model.addAttribute(articleRepository.findById(articleId));
		return "article";
	}

	@DeleteMapping("articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId) {
		articleRepository.deleteById(articleId);
		return "redirect:/";
	}
}




