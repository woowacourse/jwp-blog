package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.annotation.UserFromSession;
import techcourse.myblog.controller.message.ResponseMessage;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.dto.domain.CommentDTO;
import techcourse.myblog.service.dto.response.CommentResponseDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public CommentResponseDto create(@RequestBody CommentDTO commentDTO,
                                     @PathVariable long articleId,
                                     @UserFromSession User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        Comment comment = commentDTO.toDomain(article, user);
        article.add(comment);
        commentRepository.save(comment);
        return new CommentResponseDto(article, comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable long articleId,
                                                  @PathVariable long commentId,
                                                  @UserFromSession User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
        comment.validate(user);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        article.remove(comment);
        commentRepository.delete(comment);
        ResponseMessage responseMessage = new ResponseMessage("success", "", "", "");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseMessage> update(@PathVariable long articleId,
                                                  @PathVariable long commentId,
                                                  @RequestBody CommentDTO commentDTO,
                                                  @UserFromSession User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
        comment.validate(user);

        comment.update(commentDTO.toDomain(comment.getArticle(), comment.getAuthor()));
        ResponseMessage responseMessage = new ResponseMessage("success", "", "", "");

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> exceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("commentError", exception.getMessage());
        ResponseMessage responseMessage = new ResponseMessage("", "",
                exception.getMessage(), "");
        log.error("error : {}", exception.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
