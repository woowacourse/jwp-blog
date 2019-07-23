package techcourse.myblog.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.domain.Article.ArticleException;
import techcourse.myblog.web.ArticleController;

@ControllerAdvice(assignableTypes = ArticleController.class)
public class ArticleControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerAdvice.class);

    @ExceptionHandler(ArticleException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String failToLogin(ArticleException e, Model model) {
        log.debug("Article Manipulate FAILED {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "404";
    }
}
