package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.repository.CommentRepository;

@RequestMapping("/comments")
@RestController
public class CommentRestController {
    @Autowired
    CommentRepository commentRepository;

    @GetMapping
    public List<CommentResponse> showAllComments(@RequestParam Long articleId) {
        List<Comment> savedComments = commentRepository.findAllByArticleId(articleId);
        List<CommentResponse> comments = new ArrayList<>();
        savedComments.forEach(savedComment -> {
            Long id = savedComment.getId();
            String contents = savedComment.getContents();
            Long authorId = savedComment.getAuthor().getId();
            String authorName = savedComment.getAuthor().getName();
            CommentResponse commentResponse = new CommentResponse(id, contents, authorId, authorName);
            comments.add(commentResponse);
        });
        return comments;
    }
}
