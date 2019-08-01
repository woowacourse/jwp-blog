package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleAssembler;
import techcourse.myblog.domain.User;
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

    public Article getArticleById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NoSuchElementException::new);
    }

    public Article save(ArticleDto articleDto, User user) {
        Article newArticle = ArticleAssembler.writeArticle(articleDto, user);
        return articleRepository.save(newArticle);
    }

    @Transactional
    public Article update(ArticleDto articleDto, User user) {
        Article article = articleRepository.findById(articleDto.getArticleId())
                .orElseThrow(NoSuchElementException::new);
        article.checkCorrespondingAuthor(user);
        Article updatedArticle = new Article(articleDto.getArticleId(), articleDto.getTitle(),
                articleDto.getCoverUrl(), articleDto.getContents(), user);
        return article.update(updatedArticle);
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
