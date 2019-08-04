package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.IllegalCommentDeleteRequestException;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private static final String ERROR_COMMENT_NOT_FOUND_MESSAGE = "찾는 댓글이 존재하지 않습니다!";
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public Comment save(CommentSaveRequestDto commentSaveRequestDto, User user) {
        Long articleId = commentSaveRequestDto.getArticleId();
        Article article = articleService.findById(articleId);

        Comment comment = Comment.builder()
                .contents(commentSaveRequestDto.getContents())
                .article(article)
                .user(user)
                .build();
        log.debug("save comment={}", comment);

        return commentRepository.save(comment);
    }

    public List<Comment> findByArticleId(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    public Long findArticleIdById(Long id) {
        Comment comment = findById(id);
        Article article = comment.getArticle();

        return article.getId();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("error find comment by id={}", id);
                    return new CommentNotFoundException();
                });
    }

    @Transactional
    public void update(Long id, String editedContents, User user) {
        if (isCommentNotFound(id)) {
            log.error("update article request by illegal article id={}", id);
            throw new CommentNotFoundException(ERROR_COMMENT_NOT_FOUND_MESSAGE);
        }

        Comment comment = findById(id);
        comment.update(editedContents, user);
        log.debug("update comment id={}, editedContents={}", id, editedContents);
    }

    @Transactional
    public void deleteById(Long id, User user) {
        if (isCommentNotFound(id)) {
            log.error("delete comment request by illegal article id={}", id);
            throw new CommentNotFoundException(ERROR_COMMENT_NOT_FOUND_MESSAGE);
        }

        Comment comment = findById(id);
        if (comment.isNotUser(user)) {
            log.error("delete comment request by illegal user id={}, article id={}", user.getId(), id);
            throw new IllegalCommentDeleteRequestException();
        }

        commentRepository.deleteById(id);
        log.debug("delete comment id={}", id);
    }

    private boolean isCommentNotFound(Long id) {
        return findById(id) == null;
    }
}
