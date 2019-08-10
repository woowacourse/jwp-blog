package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.repository.ArticleRepository;

@Service
@Transactional
public class ArticleWriteService {
    private final ArticleRepository articleRepository;
    private final ArticleReadService articleReadService;

    @Autowired
    public ArticleWriteService(ArticleRepository articleRepository, ArticleReadService articleReadService) {
        this.articleRepository = articleRepository;
        this.articleReadService = articleReadService;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void update(Long articleId, Article article) {
        Article originArticle = articleReadService.findByIdAndAuthor(articleId, article.getAuthor());
        originArticle.update(article);
    }
}
