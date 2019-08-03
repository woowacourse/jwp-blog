package techcourse.myblog.service.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleAssembler;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.user.UserResponseDto;
import techcourse.myblog.domain.user.UserAssembler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static techcourse.myblog.domain.article.ArticleAssembler.convertToEntity;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(final ArticleRepository articleRepository, final UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return Collections.unmodifiableList(articleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Article findById(final Long id) {
        return articleRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(ArticleNotFoundException::new);
    }

    public Long save(final ArticleRequestDto articleDTO, final Long authorId) {
        User author = userRepository.findById(authorId)
            .orElseThrow(UserNotFoundException::new);
        Article article = convertToEntity(articleDTO, author);
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    public void update(final Long id, final ArticleRequestDto articleDto) {
        Objects.requireNonNull(articleDto);
        articleRepository.findById(Objects.requireNonNull(id))
            .ifPresent((retrieveArticle -> retrieveArticle.update(new Article(
                articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents(),
                retrieveArticle.getAuthor()))));
    }

    public void delete(final Long id) {
        articleRepository.deleteById(Objects.requireNonNull(id));
    }
}
