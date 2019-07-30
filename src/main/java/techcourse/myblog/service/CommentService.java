package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.IllegalArticleUpdateRequestException;
import techcourse.myblog.exception.IllegalCommentUpdateRequestException;
import techcourse.myblog.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public Comment save(CommentSaveRequestDto commentSaveRequestDto, User user) {
        Long articleId = commentSaveRequestDto.getArticleId();
        Article article = articleService.findById(articleId);

        Comment comment = Comment.builder()
                .contents(commentSaveRequestDto.getComment())
                .article(article)
                .user(user)
                .build();
        log.debug("save comment={}", comment);

        return commentRepository.save(comment);
    }

    public List<Comment> findByArticleId(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    @Transactional
    public void update(Long id, String editedContents, User user) {
        Comment comment = findById(id);
        if (!comment.isUser(user)) {
            log.debug("update comment request by illegal user id={}, comment id={}, editedContents={}"
                    , user.getId(), id, editedContents);
            throw new IllegalCommentUpdateRequestException();
        }

        comment.update(editedContents);
        log.debug("update comment id={}, editedContents={}", id, editedContents);
    }

    public Long findArticleIdById(Long id) {
        Comment comment = findById(id);
        return comment.getArticle().getId();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("error find comment by id={}", id);
                    return new CommentNotFoundException();
                });
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
        log.debug("delete comment id={}", id);
    }
}
