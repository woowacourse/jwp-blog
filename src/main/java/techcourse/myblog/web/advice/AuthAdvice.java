package techcourse.myblog.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.service.exception.NoPermissionArticleException;
import techcourse.myblog.service.exception.NoPermissionCommentException;
import techcourse.myblog.service.exception.WrongEmailAndPasswordException;
import techcourse.myblog.web.ArticleController;

@ControllerAdvice
public class AuthAdvice {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @ExceptionHandler(WrongEmailAndPasswordException.class)
    public String wrongEmailAndPassword(WrongEmailAndPasswordException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoPermissionArticleException.class)
    public String authArticleAdvice(NoPermissionArticleException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoPermissionCommentException.class)
    public String authCommentAdvice(NoPermissionCommentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
