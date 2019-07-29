package techcourse.myblog.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.controller.argumentresolver.Redirection;
import techcourse.myblog.exception.CommentException;
import techcourse.myblog.exception.IllegalRequestException;
import techcourse.myblog.exception.NotFoundCommentException;

@Slf4j
@ControllerAdvice
public class CommentExceptionAdvice {

    @ExceptionHandler({NotFoundCommentException.class, IllegalRequestException.class})
    public RedirectView commentExceptionHandler(CommentException e, RedirectAttributes redirectAttributes, Redirection redirection) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView(redirection.getRedirectUrl());
    }

}
