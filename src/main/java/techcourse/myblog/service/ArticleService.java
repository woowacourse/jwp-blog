package techcourse.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDto;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private final UserService userService;

    private final ArticleRepository articleRepository;

    public ArticleService(final UserService userService, final ArticleRepository articleRepository) {
        this.userService = userService;
        this.articleRepository = articleRepository;
    }

    public Long save(final Long userId, final ArticleDto.Request articleDto) {
        User author = userService.findById(userId);

        Article article = articleDto.toArticle(author);

        return articleRepository.save(article).getId();
    }

    public Long edit(final Long userId, final ArticleDto.Request articleDto) {
        Article article = findById(articleDto.getId());

        article.isWrittenBy(userId);
        article.update(articleDto.toArticle());

        return article.getId();
    }

    public void deleteById(Long userId, Long articleId) {
        Article article = findById(articleId);

        article.isWrittenBy(userId);
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public ArticleDto.Response getOne(Long id) {
        final Article article = findById(id);

        ArticleDto.Response articleDto = ArticleDto.Response.createBy(article);
        return articleDto;
    }

    @Transactional(readOnly = true)
    public ArticleDto.Response getOne(final Long userId, final Long articleId) {
        Article article = findById(articleId);

        article.isWrittenBy(userId);
        ArticleDto.Response articleDto = ArticleDto.Response.createBy(article);
        return articleDto;
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 글입니다." + id));
    }

    @Transactional(readOnly = true)
    public List<Article> findAllByAuthor(String author) {
        if (author != null) {
            User user = userService.findByEmail(author);
            return articleRepository.findAllByAuthor(user);
        }
        return articleRepository.findAll();
    }
}
