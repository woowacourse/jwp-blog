package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.repository.ArticleRepository;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void deleteByUserId(Long userId) {
        articleRepository.deleteByUserId(userId);
    }
}
