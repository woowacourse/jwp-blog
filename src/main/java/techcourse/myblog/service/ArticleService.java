package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.converter.ToArticle;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ArticleService {
    private static final String NOT_EXIST_ARTICLE = "해당 기사가 없습니다.";
    private final ArticleRepository articleRepository;
    private final ToArticle toArticle;

    public ArticleService(ArticleRepository articleRepository, ToArticle toArticle) {
        this.articleRepository = articleRepository;
        this.toArticle = toArticle;
    }

    public List<Article> findAll() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(NOT_EXIST_ARTICLE));
    }

    @Transactional
    public Article save(ArticleRequestDto articleRequestDto) {
        Article article = toArticle.convert(articleRequestDto);
        articleRepository.save(article);
        return article;
    }

    @Transactional(rollbackFor = ArticleException.class)
    public Article update(long articleId, ArticleRequestDto articleRequestDto) {
        Article originArticle = findArticle(articleId);
        originArticle.update(toArticle.convert(articleRequestDto));
        return originArticle;
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}