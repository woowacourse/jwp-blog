package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;

import java.util.List;

@Slf4j
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article findArticleById(long id) {
        return articleRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Comment> findAllCommentsByArticleId(long id) {
        return commentRepository.findAllByArticle_Id(id);
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
