package techcourse.myblog.article.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.dto.ArticleDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public List<ArticleDto.Response> findAll() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleDto.Response.class))
                .collect(Collectors.toList());
    }

    public long save(ArticleDto.Create articleDto) {
        Article newArticle = articleDto.toArticle();
        return articleRepository.save(newArticle).getId();
    }

    public ArticleDto.Response findById(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return modelMapper.map(article, ArticleDto.Response.class);
    }

    public long update(long articleId, ArticleDto.Update articleDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        Article updatedArticle = articleDto.toArticle(article.getId());
        return articleRepository.save(updatedArticle).getId();
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
