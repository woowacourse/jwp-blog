package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.domain.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    //TODO : 인터셉터
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    private final CommentService commentService;

    @PostMapping
    public String create(HttpSession httpSession, CommentDto.Create commentDto) {
        //TODO : OPTIONAL 제거 (질문)
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        User user = userRepository.findById(userDto.getId()).get();
        Article article = articleRepository.findById(commentDto.getArticleId()).get();

        Comment comment = commentDto.toComment(user, article);

        commentRepository.save(comment);

        return "redirect:/articles/"+commentDto.getArticleId();
    }

    @PutMapping("/{id}")
    public String update(HttpSession httpSession, CommentDto.Update commentDto){
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");

        commentService.update(userDto, commentDto);
        return "redirect:/articles/"+commentDto.getArticleId();
    }

    @DeleteMapping("/{id}")
    public String delete(HttpSession httpSession, CommentDto.Delete commentDto){
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        commentService.delete(userDto, commentDto);
        return "redirect:/articles/"+commentDto.getArticleId();
    }
}
