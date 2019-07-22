package techcourse.myblog.domain;

import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.exception.UnFoundArticleException;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Article select(long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (!article.isPresent()) {
            throw new UnFoundArticleException("해당 게시글이 존재하지 않습니다.");
        }
        return article.get();
    }

    public void update(long id, Article updateArticle) {
        Optional<Article> article = articleRepository.findById(id);
        if (!article.isPresent()) {
            throw new UnFoundArticleException("해당 게시글이 존재하지 않습니다.");
        }
        article.get().update(updateArticle);
    }

    public void delete(long id) {
        Optional<Article> article = articleRepository.findById(id);
        article.ifPresent(articleRepository::delete);
    }
}
