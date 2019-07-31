package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Slf4j
@Service
public class IndexService {
    private final ArticleRepository articleRepository;

    public IndexService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }
}
