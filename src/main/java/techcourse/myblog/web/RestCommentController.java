package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RestCommentController {
    private CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{id}")
    public List<CommentResponseDto> readComments(@PathVariable("id") Long id) {
        return commentService.findCommentsByArticleId(id);
    }

    @PostMapping("/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public void createComment(@RequestBody CommentRequestDto commentRequestDto,
                              @LoggedUser UserPublicInfoDto userPublicInfoDto) {
        Long userId = userPublicInfoDto.getId();
        commentService.save(userId, commentRequestDto);
    }

    @PutMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@PathVariable("commentId") Long commentId,
                              @RequestBody CommentRequestDto commentRequestDto,
                              @LoggedUser UserPublicInfoDto userPublicInfoDto) {

        Long userId = userPublicInfoDto.getId();
        commentService.update(userId, commentId, commentRequestDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteComment(@PathVariable("commentId") Long commentId,
                              @LoggedUser UserPublicInfoDto userPublicInfoDto) {
        Long userId = userPublicInfoDto.getId();
        commentService.delete(userId, commentId);
    }

}
