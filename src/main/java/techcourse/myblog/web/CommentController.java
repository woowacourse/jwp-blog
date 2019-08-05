package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.resolver.Session;
import techcourse.myblog.resolver.UserSession;
import techcourse.myblog.service.CommentService;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public String create(@Session UserSession userSession, CommentDto.Create commentDto) {
        commentService.save(userSession.getUserDto(), commentDto);
        return "redirect:/articles/"+commentDto.getArticleId();
    }

    @PutMapping("/{commentId}")
    public String update(@PathVariable Long commentId, @Session UserSession userSession, CommentDto.Update commentDto) {
        commentService.update(userSession.getUserDto(), commentId, commentDto);
        return "redirect:/articles/"+commentDto.getArticleId();
    }

    @DeleteMapping("/{commentId}")
    public String delete(@PathVariable Long commentId, @Session UserSession userSession, CommentDto.Response commentDto) {
        commentService.deleteById(userSession.getUserDto(), commentId);
        return "redirect:/articles/"+commentDto.getArticleId();
    }
}
