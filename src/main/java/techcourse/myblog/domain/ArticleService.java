package techcourse.myblog.domain;

import org.springframework.stereotype.Service;

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
}
