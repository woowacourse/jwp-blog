package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Service
public class IndexService {
    private final ArticleRepository articleRepository;

    public IndexService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> finaAll() {
        return articleRepository.findAll();
    }
}
