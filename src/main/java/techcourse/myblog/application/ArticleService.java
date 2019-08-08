package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new NoArticleException("게시글이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Long post(ArticleDto articleDto, Long authorId) {
        User author = userService.findById(authorId);

        Article article = articleDto.toEntity(author);
        article.setAuthor(author);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    @Transactional(readOnly = true)
    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
            .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
    }

    @Transactional(readOnly = true)
    public Article findArticleWrittenByUser(Long articleId, Long userId) {
        Article article = findArticleById(articleId);

        if (!article.matchAuthorId(userId)) {
            throw new NotSameAuthorException("해당 게시물의 작성자가 아닙니다.");
        }

        return article;
    }

    public void editArticle(ArticleDto articleDto, Long articleId, Long userId) {
        Article article = findArticleById(articleId);

        checkAuthenticatedAuthor(article, userId);

        article.updateArticle(articleDto.toEntity(article.getAuthor()));
    }

    public void deleteById(Long articleId, Long userId) {
        Article article = findArticleById(articleId);
        checkAuthenticatedAuthor(article, userId);

        articleRepository.deleteById(articleId);
    }

    private void checkAuthenticatedAuthor(Article article, Long userId) {
        if (!article.matchAuthorId(userId)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }
    }
}
