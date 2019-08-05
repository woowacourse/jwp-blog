package techcourse.myblog.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.*;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException() {
        return "redirect:/";
    }

    @ExceptionHandler(UserNotMatchedException.class)
    public String handleUserNotMatchedException() {
        return "redirect:/";
    }

    @ExceptionHandler(CommentNotMatchedException.class)
    public String handleCommentNotMatchedException() {
        return "redirect:/";
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException() {
        return "redirect:/";
    }

    @ExceptionHandler(LoginNotMatchedException.class)
    public String handleLoginNotMatchedException(LoginNotMatchedException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }
}
