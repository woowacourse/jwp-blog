package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public String create(HttpSession httpSession, CommentDto.Create commentDto) {
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        commentService.save(userDto, commentDto);
        return "redirect:/articles/"+commentDto.getArticleId();
    }

    @PutMapping("/{commentId}")
    public String update(@PathVariable Long commentId, HttpSession httpSession, CommentDto.Update commentDto){
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        commentService.update(userDto, commentId, commentDto);
        return "redirect:/articles/"+commentDto.getArticleId();
    }

    @DeleteMapping("/{commentId}")
    public String delete(@PathVariable Long commentId, HttpSession httpSession, CommentDto.Response commentDto){
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        commentService.deleteById(userDto, commentId);
        return "redirect:/articles/"+commentDto.getArticleId();
    }
}
