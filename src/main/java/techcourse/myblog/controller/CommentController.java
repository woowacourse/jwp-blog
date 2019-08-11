package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.annotation.LoginUser;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.dto.CommentResponse;
import techcourse.myblog.service.dto.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/articles/{articleId}/comments")

public class CommentController {
    public static final String RESPONSE_SUCCESS = "response success";
    public static final String RESPONSE_FAIL = "response fail";
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> show(@PathVariable long articleId) {
        List<CommentResponse> comments = commentService.getComments(articleId);
        ResponseMessage<List<CommentResponse>> responseMessage = new ResponseMessage<>(comments, RESPONSE_SUCCESS, null);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> create(@RequestBody CommentDto commentDto,
                                                  @PathVariable long articleId,
                                                  @LoginUser User user) {
        CommentResponse commentResponse = commentService.save(commentDto, articleId, user);
        ResponseMessage<CommentResponse> responseMesage = new ResponseMessage<>(commentResponse, RESPONSE_SUCCESS, null);
        return new ResponseEntity<>(responseMesage, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable long articleId,
                                                  @PathVariable long commentId,
                                                  @LoginUser User user) {
        commentService.delete(commentId, articleId, user);
        ResponseMessage responseMessage = new ResponseMessage(RESPONSE_SUCCESS, null);
        return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
    }

    @Transactional
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseMessage> update(@PathVariable long commentId,
                                                  @RequestBody CommentDto commentDto,
                                                  @LoginUser User user) {
        commentService.update(commentId, commentDto, user);
        ResponseMessage responseMessage = new ResponseMessage(RESPONSE_SUCCESS, null);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> exceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("commentError", exception.getMessage());
        ResponseMessage responseMessage = new ResponseMessage(RESPONSE_FAIL, exception.getMessage());
        log.error("error : {}", exception.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
