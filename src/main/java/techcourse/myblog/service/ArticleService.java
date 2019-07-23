package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.web.exception.UserArticleMissmatchException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
        return toArticleDto(articleRepository.save(articleDto.toEntity()));
    }

    public void update(Long articleId, Long userId, ArticleDto articleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        if (!article.matchUserId(userId)) {
            throw new UserArticleMissmatchException();
        }
        article.updateArticle(articleDto.toEntity());
        articleRepository.save(article);
    }

    public void delete(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        if (!article.matchUserId(userId)) {
            throw new UserArticleMissmatchException();
        }
        articleRepository.deleteById(articleId);
    }

    private ArticleDto toArticleDto(Article article) {
        return new ArticleDto(article.getId(),
                article.getUserId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents());
    }
}
