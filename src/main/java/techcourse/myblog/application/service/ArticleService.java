package techcourse.myblog.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Long save(ArticleDto articleDto) {
        return articleRepository.save(new Article.ArticleBuilder()
                .contents(articleDto.getContents())
                .coverUrl(articleDto.getCoverUrl())
                .title(articleDto.getTitle())
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));

        return ArticleDto.of(article);
    }

    @Transactional
    public void deleteById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void modify(Long articleId, ArticleDto articleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException(""));

        article.changeContents(articleDto.getContents());
        article.changeCoverUrl(articleDto.getCoverUrl());
        article.changeTitle(articleDto.getTitle());
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        List<ArticleDto> articles = new ArrayList<>();

        articleRepository.findAll().forEach(article -> articles.add(ArticleDto.of(article)));

        return articles;
    }
}
