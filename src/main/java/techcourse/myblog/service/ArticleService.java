package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.converter.ToArticle;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private static final String NOT_EXIST_ARTICLE = "해당 기사가 없습니다.";
    private static final String ID = "id";
    private static final int VIEW_ARTICLE_COUNT = 10;
    private static final int START_PAGE = 0;
    private final ArticleRepository articleRepository;
    private final ToArticle toArticle;

    public ArticleService(ArticleRepository articleRepository, ToArticle toArticle) {
        this.articleRepository = articleRepository;
        this.toArticle = toArticle;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        PageRequest request = PageRequest.of(START_PAGE, VIEW_ARTICLE_COUNT, Sort.Direction.DESC, ID);
        Page<Article> page = articleRepository.findAll(request);
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
        Article article = toArticle.convert(articleRequestDto);
        articleRepository.save(article);
        return article;
    }

    @Transactional(rollbackFor = ArticleException.class)
    public Article update(long articleId, ArticleRequestDto articleRequestDto) {
        Article originArticle = findArticle(articleId);
        originArticle.update(toArticle.convert(articleRequestDto));
        return originArticle;
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}