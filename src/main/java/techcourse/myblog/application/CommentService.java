package techcourse.myblog.application;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.exception.*;
import techcourse.myblog.domain.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public Comment save(CommentRequest commentRequest, Long articleId, Long userId) {
        if (StringUtils.isBlank(commentRequest.getContents())) {
            throw new EmptyCommentRequestException();
        }

        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new NoArticleException("게시글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        Comment comment = new Comment(commentRequest.getContents(), user, article);

        return commentRepository.save(comment);
    }

    public List<Comment> findCommentsByArticle(Article article) {
        return Collections.unmodifiableList(commentRepository.findAllByArticle(article));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다."));
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findCommentById(commentId);
        User author = userRepository.findById(userId)
            .orElseThrow(() -> new NoUserException("존재하지 않는 회원입니다."));

        if (!comment.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void updateComment(Long commentId, Long userId, CommentRequest commentRequest) {
        Comment comment = findCommentById(commentId);
        User author = userRepository.findById(userId)
            .orElseThrow(() -> new NoUserException("존재하지 않는 회원입니다."));
        Comment updatedComment = commentRequest.toEntity(author, comment.getArticle());

        try {
            comment.updateContents(updatedComment, author);
        } catch (IllegalArgumentException e) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 수정할 수 있습니다.");
        }
    }

}
