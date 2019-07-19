package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
        model.addAttribute("articles", articleService.findAll());
        /*User userDto = (User)request.getSession(true);
        if(userDto != null ){
            model.addAttribute("user",userDto);
        }*/
        return "index";
    }
}
