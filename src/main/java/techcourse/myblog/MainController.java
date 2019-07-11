package techcourse.myblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleDTO;
import techcourse.myblog.domain.ArticleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles1 = articleRepository.findAll();
        List<ArticleDTO> articles = new ArrayList<>();
        for (Article article : articles1) {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setArticleId(article.getArticleId());
            articleDTO.setTitle(article.getTitle());
            articleDTO.setCoverUrl(article.getCoverUrl());
            articleDTO.setContents(article.getContents());
            articles.add(articleDTO);
        }
        Collections.reverse(articles);
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String writing(@ModelAttribute ArticleDTO articleDTO) {
        return "article-edit";
    }
}
