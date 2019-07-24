package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);
    @ExceptionHandler(Exception.class)
    public ModelAndView common(Exception e) {
        logger.error(e.toString());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/error");
        mv.addObject("error", e);
        return mv;
    }
}
