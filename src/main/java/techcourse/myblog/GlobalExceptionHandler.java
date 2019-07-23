package techcourse.myblog;

import techcourse.myblog.exception.LoginException;
import techcourse.myblog.exception.NotFoundArticleException;

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
		return "/login";
	}

	@ExceptionHandler(value = NotFoundArticleException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleArticleException(NotFoundArticleException articleException, Model model) {
		model.addAttribute("error", articleException.getMessage());
		return "index";
	}
}
