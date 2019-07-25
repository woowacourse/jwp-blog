package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

import java.util.Optional;

@Service
@Transactional
public class ArticleWriteService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleWriteService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void update(Long articleId, ArticleDto articleDto) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        articleOptional.ifPresent(article -> article.update(articleDto.toArticle()));
    }
}
