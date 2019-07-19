package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import javax.transaction.Transactional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Long create(ArticleDto articleDto) {
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .coverUrl(articleDto.getCoverUrl())
                .contents(articleDto.getContents())
                .build();
        articleRepository.save(article);
        return article.getId();
    }

    public Article find(Long id) {
        return articleRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public Article update(Long id, ArticleDto articleDto) {
        Article article = articleRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        article.update(articleDto);
        return article;
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
