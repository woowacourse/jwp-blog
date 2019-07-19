package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleException;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private static final String NOT_EXSIT_ARTICLE = "해당 아티클이 없습니다.";
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    public Article findArticle(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(NOT_EXSIT_ARTICLE));
    }

    public Long save(ArticleDto articleDto) {
        Article article = articleDto.toEntity();
        articleRepository.save(article);
        return article.getId();
    }

    public Article update(long articleId, ArticleDto articleDto) {
        Article originArticle = findArticle(articleId);
        originArticle.update(articleDto.toEntity());
        articleRepository.save(originArticle);
        return originArticle;
    }

    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
