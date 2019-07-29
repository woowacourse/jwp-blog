package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.exception.NotFoundArticleException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(this::toArticleDto)
                .collect(Collectors.toList());
    }

    public ArticleDto findById(Long id) {
        return toArticleDto(articleRepository.findById(id)
                .orElseThrow(NotFoundArticleException::new));
    }

    public ArticleDto save(ArticleDto articleDto) {
        User author = userService.findById(articleDto.getUserId());
        return toArticleDto(articleRepository.save(articleDto.toEntity(author)));
    }

    public void update(Long articleId, Long userId, ArticleDto articleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        if (article.matchUserId(userId)) {
            article.updateArticle(articleDto.toVo());
            articleRepository.save(article);
        }
    }

    public void delete(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        if (article.matchUserId(userId)) {
            articleRepository.deleteById(articleId);
        }
    }

    private ArticleDto toArticleDto(Article article) {
        return new ArticleDto(article.getId(),
                article.getAuthorId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents());
    }
}
