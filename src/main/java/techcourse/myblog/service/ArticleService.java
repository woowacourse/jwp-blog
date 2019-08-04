package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public Long save(final Long userId, final ArticleDto.Request articleDto) {
        User author = userService.getUserById(userId);

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
        articleDto.setComments(article.getComments());
        return articleDto;
    }

    @Transactional(readOnly = true)
    public ArticleDto.Response getOne(final Long userId, final Long articleId) {
        Article article = findById(articleId);

        article.isWrittenBy(userId);
        ArticleDto.Response articleDto = ArticleDto.Response.createBy(article);
        articleDto.setComments(article.getComments());
        return articleDto;
    }

    private Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 글입니다." + id));
    }

    public List<Article> findAllByAuthor(String author) {
        if(author != null){
            User user = userService.findByEmail(author);
            return articleRepository.findAllByAuthor(user);
        }
        return articleRepository.findAll();
    }
}
