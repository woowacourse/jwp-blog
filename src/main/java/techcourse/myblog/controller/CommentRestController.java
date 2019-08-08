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
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.service.CommentRestService;

@RequestMapping("/comments")
@RestController
public class CommentRestController {

    private CommentRestService commentRestService;
    private UserSessionManager userSessionManager;

    @Autowired
    public CommentRestController(CommentRestService commentRestService,
                                 UserSessionManager userSessionManager) {
        this.commentRestService = commentRestService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping
    public List<CommentResponse> showAllComments(@RequestParam Long articleId) {
        return commentRestService
                .findAllByArticleId(articleId)
                .stream()
                .map(savedComment -> new CommentResponse(savedComment, savedComment.getAuthor()))
                .collect(Collectors.toList());
    }

    @GetMapping("/total")
    public int getCountOfComment(@RequestParam Long articleId) {
        return commentRestService.getCommentSizeByArticleId(articleId);
    }

    @PostMapping
    public CommentResponse save(@RequestBody CommentRequest commentRequest) {
        User user = userSessionManager.getUser();
        checkAuthorize(user);
        return commentRestService.save(commentRequest, user);
    }

    @PutMapping("/{commentId}")
    public CommentResponse put(@PathVariable Long commentId,
                               @RequestBody CommentRequest commentRequest) {
        User user = userSessionManager.getUser();
        return commentRestService.put(commentId, commentRequest, user);
    }

    @DeleteMapping("/{commentId}")
    public String delete(@PathVariable Long commentId) {
        User user = userSessionManager.getUser();
        commentRestService.delete(commentId, user);
        return "success!";
    }

    private void checkAuthorize(User user) {
        if (user == null) {
            throw new UnauthorizedException();
        }
    }
}
