package techcourse.myblog.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler({ArticleNotFoundException.class, CommentNotFoundException.class})
    public String handleTextNotFoundException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler(UserUpdateFailException.class)
    public String handleUpdateException(UserUpdateFailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "mypage-edit";
    }

    @ExceptionHandler(MisMatchAuthorException.class)
    public String handleMisMatchException(MisMatchAuthorException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }

}
