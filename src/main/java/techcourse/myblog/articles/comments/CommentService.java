package techcourse.myblog.articles.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.articles.Article;
import techcourse.myblog.articles.ArticleRepository;
import techcourse.myblog.users.User;
import techcourse.myblog.users.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Long save(final CommentDto.Create commentDto, final Long userId) {
        final User user = findUserById(userId);
        final Article article = findArticleById(commentDto.getArticleId());
        final Comment comment = commentDto.toComment(article, user);

        return commentRepository.save(comment).getId();
    }

    public CommentDto.Response update(final CommentDto.Update commentDto, final Long userId) {
        final Comment comment = findCommentById(commentDto.getId());
        final User user = findUserById(userId);

        comment.isWrittenBy(user);

        comment.update(commentDto.toComment());

        return CommentDto.Response.createByComment(comment);
    }

    public void delete(final Long commentId, final Long userId) {
        final Comment comment = findCommentById(commentId);
        final User user = findUserById(userId);

        comment.isWrittenBy(user);

        commentRepository.delete(comment);
    }

    private Comment findCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 아닙니다."));
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 아닙니다."));
    }

    private Article findArticleById(final Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 글이 아닙니다."));
    }
}
