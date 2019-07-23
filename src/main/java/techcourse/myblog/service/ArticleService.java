package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.article.ArticleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public long createArticle(ArticleDto articleDto) {
        Article article = articleRepository.save(articleDto.toEntity());
        return article.getId();
    }

    public ArticleDto readById(long articleId) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            return ArticleDto.from(maybeArticle.get());
        }
        throw new IllegalArgumentException("게시글을 읽을 수 없습니다.");
    }

    @Transactional
    public ArticleDto updateByArticle(long articleId, ArticleDto articleDto) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            maybeArticle.get().update(articleDto);

            return readById(articleId);
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
