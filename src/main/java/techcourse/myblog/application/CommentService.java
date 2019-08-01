package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.CommentNotFoundException;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.*;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void save(CommentRequest commentRequest, Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NoArticleException("게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        Comment comment = new Comment(commentRequest.getContents(), user, article);

        commentRepository.save(comment);
    }

    public List<Comment> findCommentsByArticle(Article article) {
        return Collections.unmodifiableList(commentRepository.findAllByArticle(article));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다."));
    }

    public void deleteComment(Long commentId, UserResponse userResponse) {
        Comment comment = findCommentById(commentId);
        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        if (!comment.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void updateComment(Long commentId, UserResponse userResponse, CommentRequest commentRequest) {
        Comment comment = findCommentById(commentId);
        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        try {
            comment.changeContents(commentRequest, author);
        } catch (IllegalArgumentException e) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 수정할 수 있습니다.");
        }
    }

}
