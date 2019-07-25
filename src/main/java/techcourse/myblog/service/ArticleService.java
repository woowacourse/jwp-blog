package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleAssembler;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDto> getAllArticles() {
        return ArticleAssembler.writeDtos(articleRepository.findAll());
    }

    public ArticleDto getArticleDtoById(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
        return ArticleAssembler.writeDto(article);
    }

    public long createArticle(ArticleDto article) {
        Article newArticle = ArticleAssembler.writeArticle(article);
        return articleRepository.save(newArticle).getArticleId();
    }

    public void updateArticle(ArticleDto articleDto) {
        articleRepository.save(ArticleAssembler.writeArticle(articleDto));
    }

    public void deleteArticleById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
