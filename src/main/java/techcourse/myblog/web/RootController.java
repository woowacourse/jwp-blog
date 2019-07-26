package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import techcourse.myblog.domain.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RootController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<ArticleDto> articles = new ArrayList<>();
        for (Article article : articleRepository.findAll()) {
            articles.add(ArticleDto.from(article));
        }
        List<CategoryDto> categories = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            categories.add(CategoryDto.from(category));
        }

        model.addAttribute("categories", categories);
        model.addAttribute("articles", articles);

        Object userId = session.getAttribute("userId");
        if (userId != null) {
            userRepository.findById((long) userId).ifPresent( user -> {
                UserDto userDto = UserDto.fromWithoutPassword(user);

                model.addAttribute("userInfo", userDto);
            });
        }

        return "index";
    }

    @GetMapping("/{categoryId}")
    public String index(@PathVariable final long categoryId, Model model) {
        List<ArticleDto> articles = new ArrayList<>();
        for (Article article : articleRepository.findByCategoryId(categoryId)) {
            articles.add(ArticleDto.from(article));
        }
        List<CategoryDto> categories = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            categories.add(CategoryDto.from(category));
        }

        model.addAttribute("categories", categories);
        model.addAttribute("articles", articles);
        System.out.println(articles);
        return "index";
    }

}
