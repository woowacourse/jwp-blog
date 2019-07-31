package techcourse.myblog.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.article.ArticleException;
import techcourse.myblog.web.ArticleController;

@ControllerAdvice(assignableTypes = ArticleController.class)
public class ArticleControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerAdvice.class);

    @ExceptionHandler(ArticleException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public String articleHandler(ArticleException e, RedirectAttributes redirectAttributes) {
        log.debug("article Manipulate FAILED {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/error";
    }
}
