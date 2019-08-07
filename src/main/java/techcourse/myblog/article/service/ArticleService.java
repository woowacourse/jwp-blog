package techcourse.myblog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.dto.ArticleRequest;
import techcourse.myblog.article.dto.ArticleResponse;
import techcourse.myblog.article.exception.ArticleNotFoundException;
import techcourse.myblog.article.presentation.ArticleRepository;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.user.User;
import techcourse.myblog.user.dto.UserResponse;
import techcourse.myblog.user.exception.UserNotFoundException;
import techcourse.myblog.user.presentation.UserRepository;
import techcourse.myblog.user.service.UserAssembler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static techcourse.myblog.article.service.ArticleAssembler.convertToEntity;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(final ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<ArticleResponse> findAll() {
        List<Article> articles = articleRepository.findAll();

        return Collections.unmodifiableList(articles.stream()
                .map(ArticleAssembler::convertToDto)
                .collect(toList()));
    }

    public ArticleResponse findById(final Long id) {
        return articleRepository.findById(Objects.requireNonNull(id))
                .map(ArticleAssembler::convertToDto)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public UserResponse findAuthor(final Long id) {
        Article retrieveArticle = articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
        return UserAssembler.convertToDto(retrieveArticle.getAuthor());
    }

    public Long save(final ArticleRequest articleRequest, final Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(UserNotFoundException::new);
        Article article = convertToEntity(articleRequest, author);
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    @Transactional
    public void update(final Long id, final ArticleRequest articleRequest, final UserResponse accessUser) {
        Article retrieveArticle = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findById(accessUser.getId()).orElseThrow(UserNotFoundException::new);
        retrieveArticle.update(convertToEntity(articleRequest, user));
    }

    public void delete(final Long id, final UserResponse accessUser) {
        Article retrieveArticle = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findById(accessUser.getId()).orElseThrow(UserNotFoundException::new);
        if (retrieveArticle.matchAuthor(user)) {
            articleRepository.delete(retrieveArticle);
            return;
        }
        throw new UserHasNotAuthorityException();
    }
}
