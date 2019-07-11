package techcourse.myblog.web;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;

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

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("articles", articleRepository.findAll());
		return "index";
	}

	@GetMapping("article/{id}")
	public String getArticle(@PathVariable int id, Model model) {
		model.addAttribute("article", articleRepository.findById(id));
		return "article";
	}

	@GetMapping("articles/{articleId}/edit")
	public String editArticle(@PathVariable int articleId, Model model) {
		model.addAttribute("article", articleRepository.findById(articleId));
		return "article-edit";
	}
}




