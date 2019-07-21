package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.web.ArticleNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAllArticle() {
        return articleRepository.findAll();
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(int id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("잘못된 입력입니다."));
    }

    public void deleteById(int id) {
        articleRepository.deleteById(id);
    }

    public Article findArticleByTitle(String title) {
        return articleRepository.findArticleByTitle(title);
    }
}
