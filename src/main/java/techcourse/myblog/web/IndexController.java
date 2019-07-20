package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.UserDto;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
        model.addAttribute("articles", articleService.findAll());

        HttpSession session = request.getSession(true);
        log.error(String.valueOf(session.getAttribute("user")));
        UserDto userDto = (UserDto) session.getAttribute("user");

        if(userDto != null){
            model.addAttribute("user", userDto);
        }
        return "index";
    }
}
