package techcourse.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.web.controller.dto.ArticleDto;

@Service
public class ArticleService {
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_CORRECTION = 1;

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public Page<Article> findAll(int page) {
        return articleRepository.findAll(
                PageRequest.of(page - PAGE_CORRECTION, PAGE_SIZE, Sort.by("id").descending()));
    }

    @Transactional(readOnly = true)
    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Article save(ArticleDto articleDto) {
        return articleRepository.save(
                new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents()));
    }

    @Transactional
    public Article update(Long articleId, ArticleDto articleDto) {
        Article selectedArticle = findById(articleId);
        return selectedArticle.update(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());
    }

    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
