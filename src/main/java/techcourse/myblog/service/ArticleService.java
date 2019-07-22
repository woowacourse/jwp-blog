package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public boolean tryRender(long articleId, Model model) {
        return articleRepository.findById(articleId).map(article -> {
                                                            model.addAttribute("article", article);
                                                            return true;
                                                        }).orElse(false);
    }

    public Iterable<Article> loadEveryArticles() {
        return articleRepository.findAll();
    }

    public long write(String title, String coverUrl, String contents) {
        return articleRepository.save(new Article(title, coverUrl, contents)).getId();
    }

    public boolean tryUpdate(long articleId, Article article) {
        return articleRepository.findById(articleId).map(ifExists -> {
                                                            article.setId(articleId);
                                                            articleRepository.save(article);
                                                            return true;
                                                        }).orElse(false);
    }

    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }
}