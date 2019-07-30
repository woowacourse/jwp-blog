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
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping("/comments/{articleId}")
    public String save(CommentDto commentDto, @PathVariable long articleId, HttpSession session) {
        Object userId = session.getAttribute("userId");
        UserDto userDto = userService.findByUserId((long) userId);
        commentService.create(articleId, commentDto, userDto);

        return "redirect:/articles/" + articleId;
    }

    @Transactional
    @PutMapping("/comments/{articleId}/{commentId}")
    public String update(CommentDto commentDto, @PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        Object userId = session.getAttribute("userId");
        commentService.checkAuthor(commentId, (long) userId);
        commentService.update(commentId, commentDto);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comments/{articleId}/{commentId}")
    public String delete(@PathVariable long articleId, @PathVariable long commentId, HttpSession session) {
        Object userId = session.getAttribute("userId");
        commentService.checkAuthor(commentId, (long) userId);
        commentService.delete(commentId);

        return "redirect:/articles/" + articleId;
    }
}
