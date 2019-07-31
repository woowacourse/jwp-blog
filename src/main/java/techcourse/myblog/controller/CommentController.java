package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.exception.MisMatchAuthorException;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

@RequestMapping("/comments")
@SessionAttributes("user")
@Controller
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    public String createComment(CommentDto commentDto, @ModelAttribute User user) {
        Article foundArticle = articleService.findById(commentDto.getArticleId());
        commentService.save(user, foundArticle, commentDto);

        return "redirect:/articles/" + foundArticle.getId();
    }

    @GetMapping("/{commentId}/edit")
    public String editCommentForm(@PathVariable Long commentId, Model model, @ModelAttribute User user) {
        checkOwner(commentId, user);
        Comment comment = commentService.findById(commentId);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @DeleteMapping("/{commentId}")
    private String deleteComment(@PathVariable Long commentId) {
        log.error("asdfid, {}", commentId);
        Comment comment = commentService.findById(commentId);
        log.error("asdfid1, {}", commentId);
        Long articleId = comment.getArticle().getId();
        log.error("asdfid2, {}", commentId);
        commentService.delete(commentId);

        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable Long commentId, CommentDto commentDto, @ModelAttribute User user) {
        checkOwner(commentId, user);
        Comment newComment = commentService.update(commentDto, commentId);
        return "redirect:/articles/" + newComment.getArticle().getId();
    }

    private void checkOwner(Long commentId, User user) {
        if (!commentService.isOwnerOf(commentId, user)) {
            throw new MisMatchAuthorException("댓글을 작성한 유저만 수정할 수 있습니다.");
        }
    }

}
