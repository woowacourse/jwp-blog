package techcourse.myblog.web;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.domain.dto.ArticleDto;
import techcourse.myblog.domain.dto.response.ArticleResponseDto;
import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.supports.UserSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
	private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

	private ArticleService articleService;

	ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@GetMapping("/new")
	public String writeArticle() {
		return "article-edit";
	}

	@PostMapping("/new")
	public String createArticle(@UserSession LoginUser user, ArticleDto articleDto) {
		ArticleResponseDto articleResponseDto = articleService.createArticle(articleDto, user);
		return "redirect:/articles/" + articleResponseDto.getArticleId();
	}

	@GetMapping("/{articleId}")
	public String showArticle(@PathVariable Long articleId, Model model) {
		ArticleResponseDto articleResponseDto = articleService.findArticleAndGetDto(articleId);
		model.addAttribute("article", articleResponseDto);
		model.addAttribute("comments", articleService.findAllComments(articleId));
		return "article";
	}

	@GetMapping("/{articleId}/edit")
	public String showUpdateArticle(@UserSession LoginUser user, @PathVariable Long articleId, Model model) {
		ArticleResponseDto articleResponseDto = articleService.findArticleAndGetDto(articleId);
		articleService.checkAvailableUpdateUser(articleId, user.getEmail());
		model.addAttribute("article", articleResponseDto);
		return "article-edit";
	}

	@PutMapping("/{articleId}")
	public String updateArticle(@UserSession LoginUser user, @PathVariable Long articleId, ArticleDto updateArticleDto) {
		articleService.updateArticle(articleId, updateArticleDto, user);
		return "redirect:/articles/" + articleId;
	}

	@DeleteMapping("/{articleId}")
	public String deleteArticle(@PathVariable Long articleId, @UserSession LoginUser user) {
		articleService.deleteArticle(articleId, user);
		return "redirect:/";
	}
}