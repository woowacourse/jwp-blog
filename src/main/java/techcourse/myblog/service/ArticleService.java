package techcourse.myblog.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.UnFoundArticleException;
import techcourse.myblog.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(Article article, User user) {
        article.setAuthor(user);
        return articleRepository.save(article);
    }

    public Article select(long id) {
        return articleRepository
                .findById(id)
                .orElseThrow(() ->
                        new UnFoundArticleException("해당 게시글이 존재하지 않습니다."));
    }

    public void update(long id, Article updateArticle) {
        articleRepository
                .findById(id)
                .orElseThrow(() ->
                        new UnFoundArticleException("해당 게시글이 존재하지 않습니다."))
                .update(updateArticle);
    }

    public void delete(long id) {
        articleRepository
                .findById(id)
                .ifPresent(articleRepository::delete);
    }
}
