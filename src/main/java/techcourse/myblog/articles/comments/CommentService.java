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
        //TODO 예외처리
        final User user = userRepository.findById(userId).get();
        final Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(article, user);
        return commentRepository.save(comment).getId();
    }

    public void update(final CommentDto.Update commentDto, final Long userSessionId) {
        //TODO 작성자 확인
        Comment comment = commentRepository.findById(commentDto.getId()).get();

        comment.update(commentDto.toComment());
    }
}
