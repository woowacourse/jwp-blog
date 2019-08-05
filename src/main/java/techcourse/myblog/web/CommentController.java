package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentEditRequestDto;
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
    public String editComment(@PathVariable Long id, @RequestBody CommentEditRequestDto commentEditRequestDto, User user) {
        String editedContents = commentEditRequestDto.getEditedContents();
        commentService.update(id, editedContents, user);
        log.info("update comment put request id={}, editedContents={}", id, editedContents);

        return "{\"editedContents\":\"" + editedContents + "\"}";
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id, User user) {
        log.info("delete comment delete request id={}", id);

        commentService.deleteById(id, user);
        return "{\"commentId\":\"" + id + "\"}";
    }
}
