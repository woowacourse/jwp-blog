package techcourse.myblog.web;

import javax.servlet.http.HttpSession;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.request.ArticleDto;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {
	private final ArticleRepository articleRepository;
	private final UserRepository userRepository;

	public ArticleController(final ArticleRepository articleRepository, final UserRepository userRepository) {
		this.articleRepository = articleRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("articles", articleRepository.findAll());
		return "index";
	}

	@GetMapping("/writing")
	public String createArticle(HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		return "article-edit";
	}

	@PostMapping("/articles")
	public String saveArticle(ArticleDto articleDto, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		User user = userRepository.findByEmail(httpSession.getAttribute("email").toString()).get();
		Article article = articleDto.valueOfArticle(user);
		Long id = articleRepository.save(article).getId();
		return "redirect:/articles/" + id;
	}
	//Todo : test
	@PutMapping("/articles/{articleId}")
	public String modifyArticle(@PathVariable Long articleId, ArticleDto articleDto, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		confirmAuthor(articleDto.getId(), httpSession);
		Article article = articleDto.valueOfArticle(articleId);
		articleRepository.save(article);
		return "redirect:/articles/" + articleId;
	}

	private void confirmAuthor(Long articleId, HttpSession httpSession) {
		User user = userRepository.findByEmail(httpSession.getAttribute("email").toString()).get();
		User articleUser = articleRepository.findById(articleId).get().getAuthor();
		if(!user.equals(articleUser)) {
			throw new UnauthorizedException();
		}
	}

	@GetMapping("/articles/{articleId}")
	public String getArticle(@PathVariable Long articleId, Model model) {
		getArticleWhenExists(articleId, model);
		return "article";
	}

	@GetMapping("/articles/{articleId}/edit")
	public String editArticle(@PathVariable Long articleId, Model model, HttpSession httpSession) {
		if (!existSession(httpSession)) {
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
	//Todo : test
	@DeleteMapping("/articles/{articleId}")
	public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
		if (!existSession(httpSession)) {
			return "redirect:/";
		}
		confirmAuthor(articleId, httpSession);
		articleRepository.deleteById(articleId);
		return "redirect:/";
	}

	private boolean existSession(HttpSession httpSession) {
		return (httpSession.getAttribute("email") != null);
	}
}




