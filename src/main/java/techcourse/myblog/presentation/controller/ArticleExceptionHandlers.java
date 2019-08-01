package techcourse.myblog.presentation.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.service.exception.NotMatchArticleAuthorException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = {"techcourse.myblog.presentation.controller"})
public class ArticleExceptionHandlers {
    @ExceptionHandler(NotMatchArticleAuthorException.class)
    public RedirectView handleMismatchAuthorError(HttpServletRequest request, RedirectAttributes redirectAttributes, NotMatchArticleAuthorException e) {
        String url = request.getRequestURL().toString();
        RedirectView redirectView = new RedirectView(url.substring(0, url.length() - 5));
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }
}
