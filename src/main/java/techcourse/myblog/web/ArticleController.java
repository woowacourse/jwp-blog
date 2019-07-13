package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleDTO;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String showMain(Model model) {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDTO> articleDTOs = articles.stream()
                .map(Article::toConvertDTO)
                .collect(Collectors.toList());
        model.addAttribute("articleDTOs", articleDTOs);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDTO articleDTO) {
        Article article = new Article(articleDTO.getTitle(), articleDTO.getCoverUrl(), articleDTO.getContents());
        articleRepository.addArticle(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleRepository.findArticleById(id).toConvertDTO());
        return "article";
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable int id, ArticleDTO articleDTO) {
        Article article = articleDTO.toConvertEntity();
        articleRepository.editArticle(id, article);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.deleteArticle(id);
        return "redirect:/";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleRepository.findArticleById(id).toConvertDTO());
        return "article-edit";
    }
}
