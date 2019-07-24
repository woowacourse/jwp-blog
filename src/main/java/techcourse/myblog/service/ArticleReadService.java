package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Optional;

@Service
public class ArticleReadService {
    private final ArticleRepository articleRepository;

    public ArticleReadService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }
}
