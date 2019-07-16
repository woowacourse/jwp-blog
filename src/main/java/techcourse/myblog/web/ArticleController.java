package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

//    @Autowired
//    void setArticleRepository(final ArticleRepository articleRepository) {
//        this.articleRepository = articleRepository;
//    } // TODO: 검색 - 생성자 주입 필드 주입

    @GetMapping("/writing")
    public String getNewArticlePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView writeNewArticle(@ModelAttribute("article") final ArticleDto articleDTO) {
        final int index = articleRepository.addArticle(articleDTO);
        return new RedirectView("/articles/" + index);
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(final Model model, @PathVariable final int articleId) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticlePage(final Model model, @PathVariable final int articleId) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView editArticle(@PathVariable final int articleId, @ModelAttribute("article") final ArticleDto articleDTO) {
        articleRepository.updateById(articleId, articleDTO);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable final int articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
