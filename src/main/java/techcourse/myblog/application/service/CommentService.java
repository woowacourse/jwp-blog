package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.NotExistCommentException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    private static CommentDto convertComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUserName(comment.getUser().getName());
        commentDto.setContents(comment.getContents());
        commentDto.setCreateDateTime(comment.getCreateDateTime());
        commentDto.setUpdateDateTime(comment.getUpdateDateTime());
        return commentDto;
    }

    @Transactional
    public long save(CommentDto commentDto, String userEmail, long articleId) {
        User user = userService.findUserByEmail(userEmail);
        Article article = articleService.findArticleById(articleId);
        Comment comment = new Comment(commentDto.getContents(), user, article);
        return commentRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(CommentService::convertComment).collect(Collectors.toList());
    }

    @Transactional
    public void update(long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotExistCommentException("해당 댓글이 존재하지 않습니다."));
        comment.modify(commentDto.getContents());
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void checkCommentWriter(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotExistCommentException("해당 댓글이 존재하지 않습니다."));
        UserService.checkEmail(comment.getUser(), email);
    }
}