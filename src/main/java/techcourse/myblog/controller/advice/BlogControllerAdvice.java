package techcourse.myblog.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.exception.*;

@ControllerAdvice
public class BlogControllerAdvice {
    @ExceptionHandler({EmailDuplicatedException.class, UserArgumentException.class})
    public String handleSignUpException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler({UserNotFoundException.class, LoginFailException.class})
    public String handleLoginException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }

    @ExceptionHandler(UserUpdateFailException.class)
    public String handleUpdateException(UserUpdateFailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "mypage-edit";
    }

    @ExceptionHandler(MisMatchAuthorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMisMatchException(MisMatchAuthorException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler({ArticleNotFoundException.class, CommentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleArticleNotFondException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
