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
    public List<ArticleResponseDto> findAll() {
        List<Article> articles = articleRepository.findAll();

        return Collections.unmodifiableList(articles.stream()
            .map(ArticleAssembler::convertToDto)
            .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public ArticleResponseDto findById(final Long id) {
        return articleRepository.findById(Objects.requireNonNull(id))
            .map(ArticleAssembler::convertToDto)
            .orElseThrow(ArticleNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findAuthor(final Long id) {
        return UserAssembler.convertToDto(articleRepository.findById(id)
            .orElseThrow(ArticleNotFoundException::new)
            .getAuthor());
    }

    public Long save(final ArticleRequestDto articleDTO, final Long authorId) {
        User author = userRepository.findById(authorId)
            .orElseThrow(UserNotFoundException::new);
        Article article = convertToEntity(articleDTO, author);
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    public void update(final Long id, final ArticleRequestDto articleDTO) {
        Objects.requireNonNull(articleDTO);
        articleRepository.findById(Objects.requireNonNull(id))
            .ifPresent((retrieveArticle -> retrieveArticle.update(new Article(
                articleDTO.getTitle(),
                articleDTO.getCoverUrl(),
                articleDTO.getContents(),
                retrieveArticle.getAuthor()))));
    }

    public void delete(final Long id) {
        articleRepository.deleteById(Objects.requireNonNull(id));
    }
}
