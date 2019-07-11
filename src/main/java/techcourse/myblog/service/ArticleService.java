package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.ArticleRepository;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
}
