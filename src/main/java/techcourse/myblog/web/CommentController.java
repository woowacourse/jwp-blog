package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDeleteResponseDto;
import techcourse.myblog.dto.CommentEditDto;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.dto.CommentSaveResponseDto;
import techcourse.myblog.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/writing")
    public CommentSaveResponseDto saveComment(@RequestBody CommentSaveRequestDto commentSaveRequestDto, User user) {
        log.info("save comment post request params={}", commentSaveRequestDto);

        return commentService.saveComment(commentSaveRequestDto, user);
    }

    @PutMapping("/{id}")
    public CommentEditDto editComment(@PathVariable Long id, @RequestBody CommentEditDto commentEditDto, User user) {
        String editedContents = commentEditDto.getEditedContents();
        commentService.update(id, editedContents, user);
        log.info("update comment put request id={}, editedContents={}", id, editedContents);

        return commentEditDto;
    }

    @DeleteMapping("/{id}")
    public CommentDeleteResponseDto deleteComment(@PathVariable Long id, User user) {
        log.info("delete comment delete request id={}", id);

        commentService.deleteById(id, user);
        return new CommentDeleteResponseDto(id);
    }
}
