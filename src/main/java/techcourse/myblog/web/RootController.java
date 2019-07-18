package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import techcourse.myblog.domain.*;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CategoryService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RootController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<ArticleDto> articles = articleService.readAll();
        List<CategoryDto> categories = categoryService.readAll();

        setModel(model, session, articles, categories);

        return "index";
    }

    @GetMapping("/{categoryId}")
    public String index(@PathVariable final long categoryId, HttpSession session, Model model) {
        List<ArticleDto> articles = articleService.readByCategoryId(categoryId);
        List<CategoryDto> categories = categoryService.readAll();

        setModel(model, session, articles, categories);

        return "index";
    }

    private void setModel(Model model, HttpSession session, List<ArticleDto> articles, List<CategoryDto> categories) {
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);

        // TODO: interceptor??
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            userService.readWithoutPasswordById((long) userId)
                    .ifPresent(userDto -> model.addAttribute("userInfo", userDto));
        }
    }

}
