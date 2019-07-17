package techcourse.myblog.domain;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleDto getArticleById(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
        return ArticleAssembler.writeDto(article);
    }

    public long createArticle(ArticleDto article) {
        Article newArticle = ArticleAssembler.writeArticle(article);
        return articleRepository.save(newArticle).getArticleId();
    }
}
