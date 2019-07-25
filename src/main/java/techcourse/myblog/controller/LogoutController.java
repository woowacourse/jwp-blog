package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("user");
        log.info("session id : {}", session.getId());

        return new RedirectView("/");
    }
}
