package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.comment.CommentDto;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/comments/{articleId}")
    public String save(CommentDto commentDto, @PathVariable long articleId, HttpSession session) {
        UserDto userDto = userService.findByUserEmail(getUserInfo(session));
        commentService.create(articleId, commentDto, userDto);

        return "redirect:/articles/" + articleId;
    }

    @Transactional
    @PutMapping("/comments/{articleId}/{commentId}")
    public String update(CommentDto commentDto, @PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        commentService.update(commentId, commentDto, getUserInfo(session));

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comments/{articleId}/{commentId}")
    public String delete(@PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        commentService.delete(commentId, getUserInfo(session));

        return "redirect:/articles/" + articleId;
    }

    private UserDto getUserInfo(HttpSession session) {
        return (UserDto) session.getAttribute(UserController.LOGIN_SESSION);
    }
}
