package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleDTO;
import techcourse.myblog.domain.ArticleRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/")
    public String article(@Valid ArticleDTO articleDTO, Model model) {
        int articleId = articleRepository.getSize() + 1;
        String articleTitle = articleDTO.getTitle();
        String articleCoverUrl = articleDTO.getCoverUrl();
        String articleContents = articleDTO.getContents();
        articleRepository.addArticle(new Article(articleId, articleTitle, articleCoverUrl, articleContents));
        model.addAttribute("articleDTO", articleDTO);
        return "article";
    }

    @GetMapping("/{articleId}")
    public String eachArticle(@PathVariable int articleId, Model model) {
        Article article = articleRepository.getArticleById(articleId);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleId(article.getArticleId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setCoverUrl(article.getCoverUrl());
        articleDTO.setContents(article.getContents());
        model.addAttribute("articleDTO", articleDTO);
        return "article";
    }
}
