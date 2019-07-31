package techcourse.myblog.web.controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ModelAndView common(Exception e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/error");
        mv.addObject("error", e);
        return mv;
    }
}
