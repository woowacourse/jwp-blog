package techcourse.myblog.web.controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.exception.LoginFailException;

@ControllerAdvice
public class CommonExceptionAdvice {
    @ExceptionHandler(LoginFailException.class)
    public RedirectView loginFail(LoginFailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return new RedirectView("/login");
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView common(Exception e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/error");
        mv.addObject("errorMessage", e.getMessage());
        return mv;
    }
}
