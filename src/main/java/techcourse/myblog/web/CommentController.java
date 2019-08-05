package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.userinfo.UserName;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.dto.CommentSaveResponseDto;
import techcourse.myblog.service.CommentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/writing")
    public CommentSaveResponseDto saveComment(@RequestBody CommentSaveRequestDto commentSaveRequestDto, User user) {
        log.info("save comment post request params={}", commentSaveRequestDto);
        commentService.save(commentSaveRequestDto, user);

        UserName userName = user.getName();
        return new CommentSaveResponseDto(commentSaveRequestDto.getContents(), commentSaveRequestDto.getArticleId(), userName.getName());
    }

    @PutMapping("/{id}")
    public String editComment(@PathVariable Long id, String editedContents, User user) {
        commentService.update(id, editedContents, user);
        log.info("update comment put request id={}, editedContents={}", id, editedContents);

        Long articleId = commentService.findArticleIdById(id);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id, User user) {
        log.info("delete comment delete request id={}", id);

        Long articleId = commentService.findArticleIdById(id);
        commentService.deleteById(id, user);
        return "redirect:/articles/" + articleId;
    }
}
