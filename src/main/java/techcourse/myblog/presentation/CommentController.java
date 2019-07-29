package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.persistence.CommentRepository;

@Slf4j
@Controller
public class CommentController {
    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @DeleteMapping("/comments/{commentId}")
    public String processDeleteComment(@PathVariable long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
        return "redirect:/articles/" + comment.getArticle().getId();
    }
}
