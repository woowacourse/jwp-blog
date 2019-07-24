package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public long save(ArticleDto articleDto) {
        Article newArticle = ArticleAssembler.writeArticle(articleDto);
        return articleRepository.save(newArticle).getArticleId();
    }

    @Transactional
    public Article update(ArticleDto articleDto) {
        Article updatedArticle = new Article(articleDto.getArticleId(), articleDto.getTitle(),
                articleDto.getCoverUrl(), articleDto.getContents());
        Article article = articleRepository.findById(updatedArticle.getArticleId())
                .orElseThrow(NoSuchElementException::new);
        return article.update(updatedArticle);
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
