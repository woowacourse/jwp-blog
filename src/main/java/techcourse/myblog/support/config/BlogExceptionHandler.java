package techcourse.myblog.support.config;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserEditRequest;
import techcourse.myblog.application.exception.*;
import techcourse.myblog.web.dto.ErrorResponse;

@ControllerAdvice
public class BlogExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public String handleLoginException(LoginException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @ExceptionHandler(EditException.class)
    public String handleEditException(EditException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        model.addAttribute("userEditRequest", new UserEditRequest());
        return "mypage-edit";
    }

    @ExceptionHandler(NotSameAuthorException.class)
    public RedirectView handleNotSameAuthorException(NotSameAuthorException e, RedirectAttributes redirectAttributes) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return new RedirectView("/");
    }


    @ExceptionHandler(NoUserException.class)
    public RedirectView handleNoUserException(NoUserException e, RedirectAttributes redirectAttributes) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return new RedirectView("/");
    }

    @ExceptionHandler(NoArticleException.class)
    public String handleNoArticleException(NoArticleException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        return "404";
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException(CommentNotFoundException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        return "404";
    }

    @ResponseBody
    @ExceptionHandler(JsonAPIException.class)
    public ResponseEntity handleJsonAPIException(JsonAPIException e) {
        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.fail(e));
    }
}
