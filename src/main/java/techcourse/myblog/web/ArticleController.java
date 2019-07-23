package techcourse.myblog.web;

import javax.servlet.http.HttpSession;

import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.request.ArticleDto;
import techcourse.myblog.exception.NotFoundArticleException;
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
	public String createArticle(HttpSession httpSession) {
		if(!existSession(httpSession)) {
			return "redirect:/";
		}
		return "article-edit";
	}
//TODO: SAVE, EDIT시 USER ID
	@PostMapping("articles")
	public String saveArticle(ArticleDto articleDto, HttpSession httpSession) {
		if(!existSession(httpSession)) {
			return "redirect:/";
		}
		Article article = articleDto.valueOfArticle();
		Long id = articleRepository.save(article).getId();
		return "redirect:/articles/" + id;
	}

	@GetMapping("articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		getArticleWhenExists(articleId, model);
		return "article";
	}

	@GetMapping("articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model, HttpSession httpSession) {
		if(!existSession(httpSession)) {
			return "redirect:/";
		}
		getArticleWhenExists(articleId, model);
		return "article-edit";
	}

	private void getArticleWhenExists(Long articleId, Model model) {
		Article article = articleRepository.findById(articleId)
				.orElseThrow(NotFoundArticleException::new);
		model.addAttribute("article", article);
	}

	@PutMapping("articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, ArticleDto articleDto, HttpSession httpSession) {
		if(!existSession(httpSession)) {
			return "redirect:/";
		}
		Article article = articleDto.valueOfArticle(articleId);
		articleRepository.save(article);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
		if(!existSession(httpSession)) {
			return "redirect:/";
		}
		articleRepository.deleteById(articleId);
		return "redirect:/";
	}

	private boolean existSession(HttpSession httpSession) {
		return (httpSession.getAttribute("email") != null);
	}
}




