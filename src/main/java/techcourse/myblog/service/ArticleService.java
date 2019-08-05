package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class ArticleService {
    private static final String ERROR_ARTICLE_NOT_FOUND_MESSAGE = "찾는 글이 존재하지 않습니다!";
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleService(final ArticleRepository articleRepository, final CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Iterable<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public Article save(ArticleSaveRequestDto articleSaveRequestDto, User author) {
        Article article = articleSaveRequestDto.toArticle(author);

        log.debug("save article={}", article);
        return articleRepository.save(article);
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(ERROR_ARTICLE_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public void update(ArticleSaveRequestDto articleSaveRequestDto, long id) {
        log.debug("update article params={}", articleSaveRequestDto);

        Article article = findById(id);
        article.update(articleSaveRequestDto.toArticle());
    }

    @Transactional
    public boolean deleteById(long id) {
        log.debug("delete article id={}", id);
        commentRepository.deleteByArticleId(id);
        articleRepository.deleteById(id);
        return !commentRepository.findByArticleId(id).isEmpty()
                && !articleRepository.findById(id).isPresent();
    }
}
