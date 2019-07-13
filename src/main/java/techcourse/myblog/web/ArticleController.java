package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleRepository articleRepository;

    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles.stream()
                .map(article -> modelMapper.map(article, ArticleDto.Response.class))
                .collect(Collectors.toList()));
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Create articleDto) {
        Article newArticle = articleDto.toArticle();
        long articleId = articleRepository.save(newArticle);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepository.findById(articleId);
        model.addAttribute("article", modelMapper.map(article, ArticleDto.Response.class));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, Model model) {
        Article article = articleRepository.findById(articleId);
        model.addAttribute("article", modelMapper.map(article, ArticleDto.Response.class));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto.Update articleDto) {
        Article updatedArticle = articleDto.toArticle(articleId);
        articleRepository.update(updatedArticle);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleRepository.delete(articleId);
        return "redirect:/";
    }
}
