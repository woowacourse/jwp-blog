package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import techcourse.myblog.controller.session.UserSessionManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.ArticleService;

@RequestMapping("/comments")
@RestController
public class CommentRestController {

    private ArticleService articleService;
    private CommentRepository commentRepository;
    private UserSessionManager userSessionManager;

    @Autowired
    public CommentRestController(ArticleService articleService, CommentRepository commentRepository, UserSessionManager userSessionManager/*UserRepository userRepository*/) {
        this.articleService = articleService;
        this.commentRepository = commentRepository;
        this.userSessionManager = userSessionManager;
    }

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

    @PostMapping
    public CommentResponse save(@RequestBody CommentRequest commentRequest) {
        User user = userSessionManager.getUser();
        Article article = articleService.select(commentRequest.getArticleId());
        Comment comment = new Comment(commentRequest.getContents(), user, article);
        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = new CommentResponse(
                savedComment.getId(),
                savedComment.getContents(),
                savedComment.getAuthor().getId(),
                savedComment.getAuthor().getName()
        );
        return commentResponse;
    }
}
