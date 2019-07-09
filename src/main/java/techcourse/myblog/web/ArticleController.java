package techcourse.myblog.web;

import techcourse.myblog.domain.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;

/*	@GetMapping("/")
	public String index() {
		return "index";
	}*/

	@GetMapping("writing")
	public String createArticle() {
		return "article-edit";
	}
}




