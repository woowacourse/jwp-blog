package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.CouldNotFindArticleIdException;
import techcourse.myblog.repository.ArticleRepository;

import javax.transaction.Transactional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(ArticleDto articleDto) {
        Article article = Article.of(articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents()
        );

        return articleRepository.save(article);
    }

    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(CouldNotFindArticleIdException::new);
    }

    @Transactional
    public Article update(Long articleId, ArticleDto articleDto) {
        Article findArticle = findArticleById(articleId);

        findArticle.updateArticle(
                Article.of(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents())
        );

        return findArticle;
    }

    public void deleteById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void setActionOfArticle(Model model, String actionRoute, String formMethod) {
        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
    }
}
