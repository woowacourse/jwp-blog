package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.ArticleRepository;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        log.debug("index : from index");

        UserDto userDto = (UserDto) session.getAttribute("user");

        if (userDto != null) {
            model.addAttribute("user", userDto);
        }
        return "index";
    }
}
