package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("articles", articleService.findAll());
        log.info(String.valueOf(session.getAttribute("user")));

        UserDto userDto = (UserDto) session.getAttribute("user");

        if (userDto != null) {
            model.addAttribute("user", userDto);
        }
        return "index";
    }
}
