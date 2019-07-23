package techcourse.myblog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.NoArticleException;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private static final String NO_ARTICLE_MESSAGE = "존재하지 않는 게시글 입니다.";

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable).getContent();
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

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new NoArticleException(NO_ARTICLE_MESSAGE));
    }

    @Transactional
    public Article update(Long id, ArticleDto articleDto) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NoArticleException(NO_ARTICLE_MESSAGE));
        article.update(articleDto);
        return article;
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
