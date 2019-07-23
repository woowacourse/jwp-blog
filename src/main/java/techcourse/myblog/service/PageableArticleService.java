package techcourse.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article.Article;
import techcourse.myblog.domain.Article.ArticleException;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticlePageableRepository;

import java.util.List;

@Service
public class PageableArticleService {
    private static final String NOT_EXSIT_ARTICLE = "해당 아티클이 없습니다.";
    private final ArticlePageableRepository articlePageableRepository;

    public PageableArticleService(ArticlePageableRepository articlePageableRepository) {
        this.articlePageableRepository = articlePageableRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAllPage(Pageable pageable) {
        Page<Article> page = articlePageableRepository.findAll(pageable);
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return articlePageableRepository.findById(articleId).orElseThrow(() -> new ArticleException(NOT_EXSIT_ARTICLE));
    }

    @Transactional
    public Long save(Article article) {
        Article newArticle = articlePageableRepository.save(article);
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
        articlePageableRepository.deleteById(articleId);
    }
}
