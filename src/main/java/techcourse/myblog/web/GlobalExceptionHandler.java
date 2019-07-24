package techcourse.myblog.web;

import techcourse.myblog.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = LoginException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleLoginException(LoginException loginException, Model model) {
		model.addAttribute("error", loginException.getMessage());
		return "login";
	}

	@ExceptionHandler(value = NotFoundArticleException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleArticleException(NotFoundArticleException articleException, Model model) {
		model.addAttribute("error", articleException.getMessage());
		return "index";
	}

	@ExceptionHandler(value = AlreadyExistEmailException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handlerExistEmailException(AlreadyExistEmailException existEmailException, Model model) {
		model.addAttribute("error", existEmailException.getMessage());
		return "signup";
	}

	@ExceptionHandler(value = NotFoundUserException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handlerExistEmailException(NotFoundUserException notFoundUserException, Model model) {
		model.addAttribute("error", notFoundUserException);
		return "index";
	}

	@ExceptionHandler(value = NotMatchPasswordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handlerExistEmailException(NotMatchPasswordException notMatchPasswordException, Model model) {
		model.addAttribute("error", notMatchPasswordException.getMessage());
		return "leave-user";
	}
}
