package techcourse.myblog.service.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.presentation.ArticleRepository;
import techcourse.myblog.presentation.CommentRepository;
import techcourse.myblog.presentation.UserRepository;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.comment.CommentResponse;
import techcourse.myblog.service.dto.user.UserResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static techcourse.myblog.service.comment.CommentAssembler.convertToDto;
import static techcourse.myblog.service.comment.CommentAssembler.convertToEntity;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public CommentResponse findById(final Long id) {
        return convertToDto(commentRepository.findById(id).orElseThrow(CommentNotFoundException::new));
    }

    public List<CommentResponse> findByArticleId(Long articleId) {
        return commentRepository.findByArticleId(articleId).stream()
                .map(CommentAssembler::convertToDto)
                .collect(toList());
    }

    public CommentResponse save(final CommentRequest commentRequest, final Long userId, final Long articleId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        Comment comment = convertToEntity(commentRequest, user, article);
        Comment persistComment = commentRepository.save(comment);
        return convertToDto(persistComment);
    }

    @Transactional
    public CommentResponse update(final CommentRequest commentRequest, final Long commentId, final Long articleId, final UserResponse accessUser) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findById(accessUser.getId()).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        comment.update(convertToEntity(commentRequest, user, article));
        return convertToDto(comment);
    }

    public void delete(final Long id, final UserResponse accessUser) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findById(accessUser.getId()).orElseThrow(UserNotFoundException::new);
        if (comment.matchAuthor(user)) {
            commentRepository.delete(comment);
            return;
        }
        throw new UserHasNotAuthorityException();
    }
}
