package techcourse.myblog.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.articles.Article;
import techcourse.myblog.articles.ArticleRepository;
import techcourse.myblog.users.User;
import techcourse.myblog.users.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Long save(final CommentDto.Create commentDto, final Long userId) {
        final User user = userRepository.findById(userId).get();
        final Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(article, user);
        return commentRepository.save(comment).getId();
    }

}
