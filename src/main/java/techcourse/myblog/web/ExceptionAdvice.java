package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(NotFoundArticleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundArticleException(NotFoundArticleException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/";
    }
    //TODO : ArticleExceptionAdvice

    @ExceptionHandler(InvalidSignUpFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidSignUpFormException(InvalidSignUpFormException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(InvalidEditFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelInvalidEditFormException(InvalidEditFormException e, HttpSession httpSession, Model model) {
        Optional<UserDto.Response> user = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        }
        model.addAttribute("errorMessage", e.getMessage()); //TODO : 리다이렉트
        return "mypage";
    }
    //TODO : 왜 일로 안올까

    @ExceptionHandler(DuplicatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelDuplicatedUserException(DuplicatedUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/signup";
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundUserException(NotFoundUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/login";
    }
    //TODO : "/login"으로 하면 에러 안나고 "/"로 하면 에러

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotMatchPasswordException(NotMatchPasswordException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/login";
    }
}
