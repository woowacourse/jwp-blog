package techcourse.myblog.web;

import java.util.Optional;

import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.request.ArticleDto;
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
	public String saveArticle(ArticleDto articleDto) {
		Article article = articleDto.valueOfArticle();
		Long id = articleRepository.save(article).getId();
		return "redirect:/articles/" + id;
	}

	@GetMapping("articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		if (getArticleWhenExists(articleId, model)) {
			return "article";
		}
		return addError(model);
	}

	@GetMapping("articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model) {
		if (getArticleWhenExists(articleId, model)) {
			return "article-edit";
		}
		return addError(model);
	}

	private boolean getArticleWhenExists(Long articleId, Model model) {
		Optional<Article> article = articleRepository.findById(articleId);
		if (article.isPresent()) {
			model.addAttribute("article", article.get());
			return true;
		}
		return false;
	}

	private String addError(Model model) {
		model.addAttribute("error", "유효하지 않은 게시글 번호입니다.");
		return "index";
	}

	@PutMapping("articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, ArticleDto articleDto) {
		Article article = articleDto.valueOfArticle(articleId);
		articleRepository.save(article);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId) {
		articleRepository.deleteById(articleId);
		return "redirect:/";
	}
}




