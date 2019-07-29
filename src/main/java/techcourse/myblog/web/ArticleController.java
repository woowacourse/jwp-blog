package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveParams;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(ArticleSaveParams articleSaveParams, HttpSession httpSession) {
        log.info("save article post request params={}", articleSaveParams);
        Article article = articleService.save(articleSaveParams.toEntity(), (User) httpSession.getAttribute(USER));
        Long id = article.getId();
        return "redirect:/articles/" + id;
    }

    @GetMapping("/{id}")
    public String fetchArticle(@PathVariable long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editArticle(@PathVariable long id, Model model, HttpSession httpSession) {
        Article article = articleService.findById(id);
        User user = (User) httpSession.getAttribute(USER);
        User author = article.getAuthor();
        
        if (!user.equals(author)) {
            return "redirect:/articles/" + id;
        }
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String saveEditedArticle(@PathVariable long id, ArticleSaveParams articleSaveParams) {
        log.info("save edited article post request params={}", articleSaveParams);
        articleService.update(articleSaveParams, id);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id) {
        log.info("delete article delete request id={}", id);
        articleService.deleteById(id);
        return "redirect:/";
    }
}
