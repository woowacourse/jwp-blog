package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.converter.DtoToArticle;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.utils.page.PageRequest;

import java.util.List;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private static final String NOT_EXIST_ARTICLE = "해당 기사가 없습니다.";
    private static final String ID = "id";
    private static final int VIEW_ARTICLE_COUNT = 10;
    private static final int START_PAGE = 0;
    private final ArticleRepository articleRepository;
    private final DtoToArticle dtoToArticle;

    public ArticleService(ArticleRepository articleRepository, DtoToArticle dtoToArticle) {
        this.articleRepository = articleRepository;
        this.dtoToArticle = dtoToArticle;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        PageRequest pageRequest = new PageRequest(START_PAGE, VIEW_ARTICLE_COUNT, Sort.Direction.DESC, ID);
        Page<Article> page = articleRepository.findAll(pageRequest.of());
        log.info("page : {} ", page.getContent());
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(NOT_EXIST_ARTICLE));
    }

    @Transactional
    public Article save(ArticleRequestDto articleRequestDto) {
        Article article = dtoToArticle.convert(articleRequestDto);
        articleRepository.save(article);
        return article;
    }

    @Transactional()
    public Article update(long articleId, ArticleRequestDto articleRequestDto) {
        Article originArticle = findArticle(articleId);
        originArticle.update(dtoToArticle.convert(articleRequestDto));
        return originArticle;
    }

    @Transactional()
    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}