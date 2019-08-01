package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.NotExistCommentException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;
import techcourse.myblog.domain.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public long save(CommentDto commentDto, String userEmail, long articleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotExistUserIdException("해당 유저가 존재하지 않습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotMatchPasswordException("해당 게시글이 존재하지 않습니다."));

        Comment comment = new Comment(commentDto.getContents(), user, article);
        return commentRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setUserName(comment.getUser().getName());
                    commentDto.setContents(comment.getContents());
                    commentDto.setLocalDate(comment.getLocalDate());
                    commentDto.setLocalTime(comment.getLocalTime());
                    return commentDto;
                }).collect(Collectors.toList());
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
    public void findCommentWriter(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotExistCommentException("해당 댓글이 존재하지 않습니다."));

        comment.getUser().checkEmail(email);
    }
}
