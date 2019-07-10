package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String articleForm(){
        return "article-edit";
    }

    @PostMapping("/articles")
    public String write(@ModelAttribute Article article){
        articleRepository.save(article);
        return "redirect:articles/"+article.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        return "article";
    }
}
