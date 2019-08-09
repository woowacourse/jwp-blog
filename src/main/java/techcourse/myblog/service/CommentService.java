package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final ArticleService articleService;
    private final UserService userService;

    private final CommentRepository commentRepository;

    public CommentService(final ArticleService articleService, final UserService userService, final CommentRepository commentRepository) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    public CommentDto.Response save(final CommentDto.Create commentDto, final Long userId) {
        final User user = userService.findById(userId);
        final Article article = articleService.findById(commentDto.getArticleId());
        Comment comment = commentDto.toComment(article, user);
        comment = commentRepository.save(comment);

        return CommentDto.Response.createByComment(comment);
    }

    public CommentDto.Response update(final CommentDto.Update commentDto, final Long userId) {
        final Comment comment = findCommentById(commentDto.getId());

        comment.isWrittenBy(userId);
        comment.update(commentDto.toComment());

        return CommentDto.Response.createByComment(comment);
    }

    public void delete(final Long commentId, final Long userId) {
        final Comment comment = findCommentById(commentId);

        comment.isWrittenBy(userId);
        commentRepository.delete(comment);
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 아닙니다."));
    }


    @Transactional(readOnly = true)
    public List<CommentDto.Response> findAllByArticleId(final Long articleId) {
        final Article article = articleService.findById(articleId);
        final List<Comment> comments = commentRepository.findAllByArticle(article);
        return comments.stream()
                .map(CommentDto.Response::createByComment)
                .collect(Collectors.toList());
    }
}
