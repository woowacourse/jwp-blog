package techcourse.myblog.service.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.presentation.ArticleRepository;
import techcourse.myblog.presentation.UserRepository;
import techcourse.myblog.service.dto.article.ArticleRequest;
import techcourse.myblog.service.dto.article.ArticleResponse;
import techcourse.myblog.service.dto.user.UserResponse;
import techcourse.myblog.service.user.UserAssembler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static techcourse.myblog.service.article.ArticleAssembler.convertToEntity;

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
                .collect(Collectors.toList()));
    }

    public ArticleResponse findById(final Long id) {
        return articleRepository.findById(Objects.requireNonNull(id))
                .map(ArticleAssembler::convertToDto)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public UserResponse findAuthor(final Long id) {
        return UserAssembler.convertToDto(articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new)
                .getAuthor());
    }

    public Long save(final ArticleRequest articleDTO, final Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(UserNotFoundException::new);
        Article article = convertToEntity(articleDTO, author);
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
