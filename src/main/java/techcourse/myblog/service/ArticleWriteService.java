package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDto;

import java.util.Optional;

@Service
public class ArticleWriteService {
    private final ArticleRepository articleRepository;

    public ArticleWriteService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Article save(ArticleDto articleDto) {
        return articleRepository.save(articleDto.toArticle());
    }

    @Transactional
    public void update(Long id, ArticleDto articleDto) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        articleOptional.ifPresent(article -> article.update(articleDto.toArticle()));
    }

    @Transactional
    public void deleteById(Long id) {
        articleRepository.findById(id);
    }
}
