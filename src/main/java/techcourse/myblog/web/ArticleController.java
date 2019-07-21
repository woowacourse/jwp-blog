package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;

import java.util.NoSuchElementException;

@Controller
public class ArticleController {
	private final ArticleRepository articleRepository;

	@Autowired
	public ArticleController(final ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		final Iterable<Article> articles = articleRepository.findAll();
		model.addAttribute("articles", articles);
		return "index";
	}

	@GetMapping("/writing")
	public String showWritingPage() {
		return "article/article-edit";
	}

	@PostMapping("/articles")
	public String save(final Article articleParam) {
		Article article = articleRepository.save(articleParam);
		return "redirect:/articles/" + article.getId();
	}

	@GetMapping("/articles/{articleId}")
	public String findById(@PathVariable final long articleId, final Model model) {
		Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
		model.addAttribute("article", article);
		return "/article/article";
	}

	@PutMapping("/articles/{articleId}")
	public String update(@PathVariable final long articleId, final Article articleParam) {
		articleParam.setId(articleId);
		articleRepository.save(articleParam);
		Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
		return "redirect:/articles/" + article.getId();
	}

	@DeleteMapping("/articles/{articleId}")
	public String delete(@PathVariable final long articleId) {
		articleRepository.deleteById(articleId);
		return "redirect:/";
	}

	@GetMapping("/articles/{articleId}/edit")
	public String editPage(@PathVariable final long articleId, final Model model) {
		Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
		model.addAttribute("article", article);
		return "/article/article-edit";
	}
}
