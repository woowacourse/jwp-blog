package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleAssembler;
import techcourse.myblog.domain.ArticleDTO;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String showMain(Model model) {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDTO> articleDTOs = new ArticleAssembler().writeDTOs(articles);
        model.addAttribute("articleDTOs", articleDTOs);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDTO articleDTO) {
        Article article = new ArticleAssembler().writeArticle(articleDTO);
        articleRepository.addArticle(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.findArticleById(id);
        ArticleDTO articleDTO = new ArticleAssembler().writeDTO(article);
        model.addAttribute("articleDTO", articleDTO);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable int id, Model model) {
        Article article = articleRepository.findArticleById(id);
        ArticleDTO articleDTO = new ArticleAssembler().writeDTO(article);
        model.addAttribute("articleDTO", articleDTO);
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable int id, ArticleDTO articleDTO) {
        articleRepository.editArticle(id, articleDTO.getTitle(), articleDTO.getCoverUrl(), articleDTO.getContents());
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.deleteArticle(id);
        return "redirect:/";
    }
}
