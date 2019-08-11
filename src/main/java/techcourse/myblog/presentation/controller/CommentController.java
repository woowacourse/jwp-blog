package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.CommentReadService;
import techcourse.myblog.application.CommentWriteService;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.presentation.support.LoginUser;

import java.util.List;

@RestController
@RequestMapping("/articles/{articleId}/comment")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private static final String DELETE_SUCCESS_MESSAGE = "삭제가 완료되었습니다.";

    private final CommentReadService commentReadService;
    private final CommentWriteService commentWriteService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentReadService commentReadService,
                             CommentWriteService commentWriteService,
                             ArticleReadService articleReadService) {
        this.commentReadService = commentReadService;
        this.commentWriteService = commentWriteService;
        this.articleReadService = articleReadService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> readComment(@PathVariable Long articleId) {
        log.debug("comment read request data : -> {}", articleId);
        return new ResponseEntity<>(commentReadService.findByArticleId(articleId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(LoginUser loginUser,
                                                            @PathVariable Long articleId,
                                                            @RequestBody CommentRequestDto commentRequestDto) {
        log.debug("comment save request data : -> {}, {}", articleId, commentRequestDto);
        Article article = articleReadService.findById(articleId);
        CommentResponseDto commentResponseDto = commentWriteService.save(commentRequestDto, loginUser.getUser(), article);
        log.debug("comment save response data : -> {}", commentResponseDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(LoginUser loginUser,
                                                            @PathVariable Long commentId,
                                                            @PathVariable Long articleId,
                                                            @RequestBody CommentRequestDto commentRequestDto) {
        log.debug("comment update request data : -> {}, {}", articleId, commentRequestDto);
        Article article = articleReadService.findById(articleId);
        CommentResponseDto commentResponseDto = commentWriteService.modify(commentId, commentRequestDto, loginUser.getUser(), article);
        log.debug("comment update response data : -> {}", commentResponseDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> removeComment(LoginUser loginUser,
                                                @PathVariable Long commentId) {
        log.debug("comment remove request data : -> {}", commentId);
        commentReadService.findById(commentId).validateAuthor(loginUser.getUser());
        commentWriteService.deleteById(commentId);
        return new ResponseEntity<>(DELETE_SUCCESS_MESSAGE, HttpStatus.OK);
    }
}
