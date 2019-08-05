package techcourse.myblog.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.comment.CommentException;
import techcourse.myblog.web.controller.CommentController;

@ControllerAdvice(assignableTypes = CommentController.class)
public class CommentControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerAdvice.class);

    @ExceptionHandler(CommentException.class)
    public String commentHandler(CommentException e, RedirectAttributes redirectAttributes) {
        log.debug("Comment Exception : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/error";
    }
}
