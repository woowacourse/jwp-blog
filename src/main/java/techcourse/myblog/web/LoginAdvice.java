package techcourse.myblog.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.service.exception.WrongEmailAndPasswordException;

@ControllerAdvice
public class LoginAdvice {

	@ExceptionHandler(WrongEmailAndPasswordException.class)
	public String wrongEmailAndPassword(WrongEmailAndPasswordException e, Model model) {
		model.addAttribute("errorMessage", e.getMessage());
		return "login";
	}
}
