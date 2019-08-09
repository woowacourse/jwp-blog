package techcourse.myblog.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.CommentNotFoundException;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.domain.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository,
                          UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public CommentResponse save(CommentRequest commentRequest, Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("게시글이 존재하지 않습니다."));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        Comment comment = new Comment(commentRequest.getContents(), author, article);
        commentRepository.save(comment);

        CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
        commentResponse.setAuthor(modelMapper.map(author, UserResponse.class));
        return commentResponse;
    }

    public List<Comment> findCommentsByArticle(Article article) {
        return Collections.unmodifiableList(commentRepository.findAllByArticle(article));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다."));
    }

    public void deleteComment(Long commentId, UserResponse userResponse) {
        Comment comment = findCommentById(commentId);
        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("존재하지 않는 회원입니다."));

        if (!comment.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UserResponse userResponse, CommentRequest commentRequest) {
        Comment comment = findCommentById(commentId);
        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("존재하지 않는 회원입니다."));
        Comment updatedComment = modelMapper.map(commentRequest, Comment.class);

        try {
            comment.updateContents(updatedComment, author);
        } catch (IllegalArgumentException e) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 수정할 수 있습니다.");
        }

        CommentResponse commentResponse = modelMapper.map(updatedComment, CommentResponse.class);
        commentResponse.setAuthor(modelMapper.map(author, UserResponse.class));
        return commentResponse;
    }
}
