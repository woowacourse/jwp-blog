package techcourse.myblog.web;

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
		Article article = articleDto.valueOfArticle(); //TODO: 여기선 아이디 없음.
		Long id = articleRepository.save(article).getId();
		return "redirect:/articles/" + id;
	}

	@GetMapping("articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		model.addAttribute("article", articleRepository.findById(articleId).get());
		return "article";
	}

	@GetMapping("articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model) {
		model.addAttribute("article", articleRepository.findById(articleId).get());
		return "article-edit";
	}

	@PutMapping("articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, ArticleDto articleDto, Model model) {
		Article article = articleDto.valueOfArticle(); //TODO: 메소드를 하나 더 만들지 말지 고려해보기
		articleRepository.save(article);
		model.addAttribute("article", articleRepository.findById(articleId).get());
		return "article";
	}

	@DeleteMapping("articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId) {
		articleRepository.deleteById(articleId);
		return "redirect:/";
	}
}




