package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;

@Slf4j
@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public CommentController(CommentRepository commentRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @DeleteMapping("/{commentId}")
    public String processDeleteComment(@PathVariable long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
        return "redirect:/articles/" + comment.getArticle().getId();
    }

//    @PutMapping("/{commentId}")
//    public String processUpdateComment(@PathVariable long commentId, CommentRequestDto commentRequestDto) {
//        return "redirect:/articles/" + commentService.update(commentId, commentRequestDto).getArticle().getId();
//    }
}
