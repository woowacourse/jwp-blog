package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.web.dto.ArticleAssembler;
import techcourse.myblog.web.dto.ArticleDto;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Controller
public class ArticleController {
    final private ArticleService articleService;

    @Autowired
    public ArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(Model model) {
        List<ArticleDto> articleDtos = articleService.findAll()
            .stream()
            .map(ArticleAssembler::convertToDto)
            .collect(Collectors.toList());
        model.addAttribute("articleDtos", articleDtos);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(final ArticleRequestDto articleDTO, @SessionAttribute(name = "user", required = false) User user) {
        Long id = articleService.save(articleDTO, user.getId());
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable final Long id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable final Long id, final ArticleRequestDto articleDTO,
                                @SessionAttribute(name = "user", required = false) User user) {
        if (user.matchId(articleService.findById(id).getAuthor().getId())) {
            articleService.update(id, articleDTO);
        }
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable final Long id,
                                @SessionAttribute(name = "user", required = false) User user) {
        if (user.matchId(articleService.findById(id).getAuthor().getId())) {
            articleService.delete(id);
            return "redirect:/";
        }
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable final Long id, Model model,
                               @SessionAttribute(name = "user", required = false) User user) {
        if (!user.matchId(articleService.findById(id).getAuthor().getId())) {
            return "redirect:/articles/" + id;
        }
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article-edit";
    }
}
