package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleDto;
import techcourse.myblog.domain.ArticleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public long createArticle(ArticleDto articleDto) {
        Article article = articleRepository.save(articleDto.toArticle());
        return article.getId();
    }

    public Optional<ArticleDto> readById(long articleId) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        return maybeArticle.map(article -> ArticleDto.from(article));
    }

    public ArticleDto updateByArticle(long articleId, ArticleDto articleDto) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            ArticleDto findArticleDto = ArticleDto.from(maybeArticle.get());
            articleDto.setId(findArticleDto.getId());

            Article article = articleRepository.save(articleDto.toArticle());
            return ArticleDto.from(article);
        }
        throw new IllegalArgumentException("업데이트 할 수 없습니다.");
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<ArticleDto> readAll() {
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (Article article : articleRepository.findAll()) {
            articleDtos.add(ArticleDto.from(article));
        }
        return articleDtos;
    }

    public List<ArticleDto> readByCategoryId(long categoryId) {
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (Article article : articleRepository.findByCategoryId(categoryId)) {
            articleDtos.add(ArticleDto.from(article));
        }
        return articleDtos;
    }
}
