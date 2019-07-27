package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.*;

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


}
