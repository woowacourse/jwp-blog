package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@ResponseBody
	@GetMapping("/articles/new")
	public String createArticle() {
		return "article-edit";
	}
}




