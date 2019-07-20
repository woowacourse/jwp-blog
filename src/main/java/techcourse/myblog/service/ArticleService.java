package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.exception.CouldNotFindArticleIdException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(ArticleDto articleDto) {
        Article article = Article.of(articleDto);

        return articleRepository.save(article);
    }

    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(CouldNotFindArticleIdException::new);
    }

    public void update(Long articleId, ArticleDto articleDto) {
        Article findArticle = findArticleById(articleId);

        findArticle.updateArticle(articleDto);
        articleRepository.save(findArticle);
    }

    public void deleteById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void setActionOfArticle(Model model, String actionRoute, String formMethod) {
        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
    }
}
