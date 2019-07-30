package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.*;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

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

    public void update(long id, Article updateArticle, User user) {
        Article article = articleRepository
                .findById(id)
                .orElseThrow(() -> new UnFoundArticleException("해당 게시글이 존재하지 않습니다."));

        if (user == null || !user.equals(article.getAuthor())) {
            throw new InvalidUserSessionException("권한이 없습니다.");
        }

        article.update(updateArticle);
    }

    public void delete(long id, User user) {
        Article article = articleRepository
                .findById(id)
                .orElseThrow(() -> new UnFoundArticleException("해당 게시글이 존재하지 않습니다."));

        if (user == null || !user.equals(article.getAuthor())) {
            throw new InvalidUserSessionException("권한이 없습니다.");
        }

        commentRepository.findAllByArticleId(id).forEach(commentRepository::delete);
        articleRepository.delete(article);
    }
}
