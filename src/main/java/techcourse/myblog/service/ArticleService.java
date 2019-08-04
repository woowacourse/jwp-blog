package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.persistence.ArticleRepository;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.exception.ArticleNotFoundException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public void update(long articleId, ArticleRequestDto articleRequestDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        article.update(articleRequestDto);
    }

    public Article save(Article newArticle) {
        return articleRepository.save(newArticle);
    }

    public Article findById(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<Article> findAll() {
        return Collections.unmodifiableList(articleRepository.findAll());
    }
}