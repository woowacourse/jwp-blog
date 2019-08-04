package techcourse.myblog.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.*;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(NotFoundArticleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundArticleException(NotFoundArticleException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "forward:/";
    }

    @ExceptionHandler(InvalidSignUpFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidSignUpFormException(InvalidSignUpFormException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(InvalidEditFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelInvalidEditFormException(InvalidEditFormException e, HttpSession httpSession, Model model) {
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        model.addAttribute("user", userDto);
        model.addAttribute("errorMessage", e.getMessage());
        return "mypage";
    }

    @ExceptionHandler(DuplicatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelDuplicatedUserException(DuplicatedUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
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

    @ExceptionHandler(NotMatchAuthorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotMatchAuthorException(NotMatchAuthorException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("path", "/");
        return "error";
    }

    //TODO : article페이지
    @ExceptionHandler(NotMatchUserException.class) //TODO: Exeception에 id / error or exception
    @ResponseStatus(HttpStatus.BAD_REQUEST) //TODO : 403, 404 .html, RedirectionAttributes
    public String handleNotMatchUserException(NotMatchUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("path", "/");
        return "error";
    }
}
