package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.service.converter.ArticleConverter;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.exception.NoArticleException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.articleConverter = ArticleConverter.getInstance();
    }

    public List<ArticleDto> findAll() {
        return articleConverter.convertToEntities(articleRepository.findAll());
    }

    public Long post(ArticleDto articleDto) {
        Article article = articleConverter.convertToEntity(articleDto);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public ArticleDto findById(long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
        return articleConverter.convertToDto(article);
    }

    @Transactional
    public void editArticle(ArticleDto articleDto, long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
        article.updateArticle(articleConverter.convertToEntity(articleDto));
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
