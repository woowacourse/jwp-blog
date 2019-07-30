package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.CommentDto;
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

    // todo 예외던지기
    public long save(CommentDto commentDto, String userEmail, long articleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(IllegalArgumentException::new);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new);

        Comment comment = new Comment(commentDto.getContents(), user, article);
        return commentRepository.save(comment).getId();
    }


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

    public void update(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(IllegalArgumentException::new);

        comment.modify(commentDto.getContents());
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void findCommentWrtier(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
        if (comment.getUser().isDifferentEmail(email)) {
            throw new IllegalArgumentException();
        }
    }
}
