package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.assembler.CommentAssembler;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.exception.UnauthorizedException;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/articles")
@RestController
public class CommentRestController {
    private ArticleService articleService;

    @Autowired
    public CommentRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}/comments")
    public List<CommentResponse> showAllComments(@PathVariable long articleId) {
        return articleService.getAllComments(articleId);
    }

    @PostMapping("/{articleId}/comments")
    public CommentResponse createComment(@PathVariable long articleId,
                                         @RequestBody CommentRequest commentRequest, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        checkAuthorize(user);
        Comment comment = articleService.saveComment(articleId, commentRequest, user);
        return CommentAssembler.writeDto(comment);
    }

    @GetMapping("/{articleId}/comments/total")
    public int getCountOfComment(@PathVariable long articleId) {
        return articleService.getAllComments(articleId).size();
    }

    private void checkAuthorize(User user) {
        if (user == null) {
            throw new UnauthorizedException();
        }
    }
}
