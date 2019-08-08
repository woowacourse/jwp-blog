package techcourse.myblog.article.handler;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.exception.NotMatchPasswordException;

@ControllerAdvice(value = {"techcourse.myblog.article.controller", "techcourse.myblog.comment.controller"})
public class ArticleExceptionAdvice {
    @ExceptionHandler(NotMatchUserException.class)
    public RedirectView handleNotMatchUserException(NotMatchUserException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(NotFoundArticleException.class)
    public RedirectView handleNotFoundArticleException(NotFoundArticleException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundUserException(NotFoundUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotMatchPasswordException(NotMatchPasswordException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }
}
