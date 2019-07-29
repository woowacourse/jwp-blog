package techcourse.myblog.articles;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.advice.AuthException;
import techcourse.myblog.users.User;
import techcourse.myblog.users.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    public Article save(final Long userId, final Article article) {
        User user = findUserById(userId);

        article.setAuthor(user);

        return articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find Article : " + id));
    }

    public Article edit(final Long userId, final Article editedArticle) {
        User user = findUserById(userId);

        Article article = findById(editedArticle.getId());

        validateAuthor(user, article);

        article.update(editedArticle);

        return article;
    }

    public void deleteById(Long userId, Long articleId) {
        User user = findUserById(userId);
        Article article = findById(articleId);

        validateAuthor(user, article);

        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Article findById(final Long userId, final Long articleId) {
        User user = findUserById(userId);

        Article article = findById(articleId);

        validateAuthor(user, article);

        return article;
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 아닙니다."));
    }

    private void validateAuthor(final User user, final Article article) {
        if (!article.isWrittenBy(user)) {
            throw new AuthException("작성자가 아닙니다.");
        }
    }
}
