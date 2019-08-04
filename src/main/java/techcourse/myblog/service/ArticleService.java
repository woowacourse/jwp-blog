package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.ArticleDeleteException;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.ArticleRequestDto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static techcourse.myblog.web.dto.ArticleAssembler.convertToEntity;

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

    public void update(final Long articleId, final ArticleRequestDto articleDto, final Long currentUserId) {
        Objects.requireNonNull(articleDto);
        articleRepository.findById(Objects.requireNonNull(articleId))
            .filter(article -> article.matchAuthor(currentUserId))
            .ifPresent((retrieveArticle ->
                retrieveArticle.update(new Article(
                    articleDto.getTitle(),
                    articleDto.getCoverUrl(),
                    articleDto.getContents(),
                    retrieveArticle.getAuthor()))));
    }

    public void delete(final Long id, final Long currentUserId) {
        articleRepository.delete(articleRepository.findById(id)
            .filter(article -> article.matchAuthor(currentUserId))
            .orElseThrow(ArticleDeleteException::new));
    }
}
