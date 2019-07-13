package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleDTO;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        if (Objects.isNull(articleRepository)) {
            throw new NullPointerException();
        }
        this.articleRepository = articleRepository;
    }

    public List<ArticleDTO> findAll() {
        List<Article> articles = articleRepository.findAll();
        return Collections.unmodifiableList(articles.stream()
                .map(Article::toConvertDTO)
                .collect(Collectors.toList()));
    }

    public ArticleDTO findArticleById(int id) {
        Article article = articleRepository.findArticleById(id);
        return article.toConvertDTO();
    }

    public int createArticle(ArticleDTO articleDTO) {
        checkNull(articleDTO);
        Article article = new Article(articleDTO.getTitle(), articleDTO.getCoverUrl(), articleDTO.getContents());
        return articleRepository.addArticle(article);
    }

    public void updateArticle(int id, ArticleDTO articleDTO) {
        checkNull(articleDTO);
        Article article = articleDTO.toConvertEntity();
        articleRepository.updateArticle(id, article);
    }

    private void checkNull(ArticleDTO articleDTO) {
        if (Objects.isNull(articleDTO)) {
            throw new NullPointerException();
        }
    }

    public void deleteArticle(int id) {
        articleRepository.deleteArticle(id);
    }
}
