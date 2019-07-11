package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/new")
    public String articleForm(){
        return "article-edit";
    }

    @PostMapping
    public String write(@ModelAttribute Article article){
        articleRepository.save(article);
        return "redirect:/articles/"+article.getId();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model){
        //TODO 에러처리
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String edit(@ModelAttribute Article article) {
        articleRepository.update(article);
        return "redirect:/articles/"+article.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "redirect:/";

    }
}
