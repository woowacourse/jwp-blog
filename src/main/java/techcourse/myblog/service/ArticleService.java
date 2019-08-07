package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.IllegalArticleDeleteRequestException;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private static final String ERROR_ARTICLE_NOT_FOUND_MESSAGE = "찾는 글이 존재하지 않습니다!";
    private final ArticleRepository articleRepository;

    public Iterable<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public Article save(ArticleSaveRequestDto articleSaveRequestDto, User author) {
        Article article = Article.builder()
                .title(articleSaveRequestDto.getTitle())
                .coverUrl(articleSaveRequestDto.getCoverUrl())
                .contents(articleSaveRequestDto.getContents())
                .author(author)
                .build();

        log.debug("save article={}", article);
        return articleRepository.save(article);
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("error find article by id={}", id);
                    return new ArticleNotFoundException(ERROR_ARTICLE_NOT_FOUND_MESSAGE);
                });
    }

    @Transactional
    public void update(ArticleSaveRequestDto articleSaveRequestDto, long id, User user) {
        Article article = findById(id);

        article.update(articleSaveRequestDto, user);
    }

    @Transactional
    public void deleteById(long id, User user) {
        Article article = findById(id);

        if (article.isNotAuthor(user)) {
            log.error("delete article request by illegal user id={}, article id={}", user.getId(), id);
            throw new IllegalArticleDeleteRequestException();
        }

        articleRepository.deleteById(id);
        log.debug("delete article id={}", id);
    }
}
