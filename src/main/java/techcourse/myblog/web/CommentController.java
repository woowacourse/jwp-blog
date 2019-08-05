package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
public class CommentController {

    private static final String USER_IS_NOT_AUTHOR = "해당 댓글의 작성자가 아닙니다.";
    private static final String COMMENT_IS_NOT_EXIST = "존재하지 않는 댓글입니다.";
    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/writing")
    public String saveComment(CommentSaveRequestDto commentSaveRequestDto, HttpSession httpSession) {
        commentService.save(commentSaveRequestDto, (User) httpSession.getAttribute(USER));

        return "redirect:/articles/" + commentSaveRequestDto.getArticleId();
    }

    @PutMapping("/comment/edit/{id}")
    public String editComment(@PathVariable Long id, String editedContents, HttpSession httpSession, Model model) {
        Long articleId = commentService.findArticleIdById(id);
        if (checkUserIsCommentAuthor(id, httpSession)) {
            commentService.update(id, editedContents);
        }
        model.addAttribute("errorMessage", USER_IS_NOT_AUTHOR);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comment/edit/{id}")
    public String deleteComment(@PathVariable Long id, HttpSession httpSession, Model model) {
        Long articleId = commentService.findArticleIdById(id);
        if (!checkUserIsCommentAuthor(id, httpSession)) {
            model.addAttribute("errorMessage", USER_IS_NOT_AUTHOR);
            return "redirect:/articles/" + articleId;
        }
        if (!commentService.deleteById(id)) {
            model.addAttribute("erroMessage", COMMENT_IS_NOT_EXIST);
            return "redirect:/articles/" + articleId;
        }
        return "redirect:/articles/" + articleId;
    }

    private boolean checkUserIsCommentAuthor(Long id, HttpSession httpSession) {
        Comment comment = commentService.findById(id);
        User user = (User) httpSession.getAttribute(USER);
        return comment.isAuthor(user);
    }
}
