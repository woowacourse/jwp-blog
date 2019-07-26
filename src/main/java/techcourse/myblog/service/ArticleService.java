package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleException;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Service
public class ArticleService {
    private static final String NOT_EXSIT_ARTICLE = "해당 아티클이 없습니다.";
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        articleRepository.findAll().forEach(articles::add);
        return articles;
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(NOT_EXSIT_ARTICLE));
    }

    @Transactional
    public Long save(Article article) {
        Article newArticle = articleRepository.save(article);
        return newArticle.getId();
    }

    @Transactional
    public Article update(long articleId, ArticleDto articleDto) {
        Article originArticle = findArticle(articleId);
        originArticle.update(articleDto.toEntity());
        return originArticle;
    }

    @Transactional
    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
