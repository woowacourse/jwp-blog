package techcourse.myblog;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleError(Exception exception, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String url = httpServletRequest.getHeader("referer");

        ModelAndView modelAndView = new ModelAndView("redirect:/");
        if (url != null) {
            modelAndView = new ModelAndView("redirect:" + url);
        }

        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return modelAndView;
    }
}
