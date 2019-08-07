package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import techcourse.myblog.controller.session.UserSessionManager;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

@RequestMapping("/comments")
@RestController
public class CommentRestController {

    private ArticleService articleService;
    private CommentService commentService;
    private UserSessionManager userSessionManager;

    @Autowired
    public CommentRestController(ArticleService articleService, CommentService commentService, UserSessionManager userSessionManager/*UserRepository userRepository*/) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping
    public List<CommentResponse> showAllComments(@RequestParam Long articleId) {
        return commentService
                .findAllByArticleId(articleId)
                .stream()
                .map(savedComment -> new CommentResponse(savedComment, savedComment.getAuthor()))
                .collect(Collectors.toList());
    }

    @GetMapping("/total")
    public int getCountOfComment(@RequestParam Long articleId) {
        return articleService.findCommentsByArticleId(articleId).size();
    }

    @PostMapping
    public CommentResponse save(@RequestBody CommentRequest commentRequest) {
        User user = userSessionManager.getUser();
        checkAuthorize(user);
        Article article = articleService.select(commentRequest.getArticleId());
        Comment comment = new Comment(commentRequest.getContents(), user, article);
        Comment savedComment = commentService.save(comment);
        return new CommentResponse(savedComment, savedComment.getAuthor());
    }

    @PutMapping("/{commentId}")
    public CommentResponse put(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        User user = userSessionManager.getUser();
        Comment editedComment = commentService.update(commentId, commentRequest, user);
        return new CommentResponse(editedComment, editedComment.getAuthor());
    }

    @DeleteMapping("/{commentId}")
    public String delete(@PathVariable Long commentId) {
        User user = userSessionManager.getUser();
        commentService.delete(commentId, user);
        return "success!";
    }

    private void checkAuthorize(User user) {
        if (user == null) {
            throw new UnauthorizedException();
        }
    }
}
