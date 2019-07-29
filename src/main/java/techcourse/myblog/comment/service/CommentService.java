package techcourse.myblog.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.comment.domain.CommentRepository;
import techcourse.myblog.comment.dto.CommentDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public void save(long articleId, long authorId, CommentDto.Creation commentDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        User author = userRepository.findById(authorId).orElseThrow(NotFoundUserException::new);
        commentRepository.save(commentDto.toComment(author, article)).getId();
    }
}
