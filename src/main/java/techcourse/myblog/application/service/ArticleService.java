package techcourse.myblog.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.ArticleConverter;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.articleConverter = ArticleConverter.getInstance();
    }

    @Transactional
    public Long save(ArticleDto articleDto) {
        return articleRepository.save(articleConverter.convertFromDto(articleDto)).getId();
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));

        return articleConverter.convertFromEntity(article);
    }

    @Transactional
    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void modify(Long articleId, ArticleDto articleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException(""));

        article.modify(articleConverter.convertFromDto(articleDto));
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        return articleConverter.createFromEntities(articleRepository.findAll());
    }
}
