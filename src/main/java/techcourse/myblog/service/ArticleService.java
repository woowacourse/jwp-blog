package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.utils.converter.ArticleConverter;
import techcourse.myblog.utils.page.PageRequest;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private static final String LOG_TAG = "[ArticleService]";

    private static final String ID = "id";
    private static final int START_PAGE = 1;
    private static final int VIEW_ARTICLE_COUNT = 10;

    private final UserService userService;
    private final ArticleRepository articleRepository;

    public ArticleService(UserService userService, ArticleRepository articleRepository) {
        this.userService = userService;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        PageRequest pageRequest = new PageRequest(START_PAGE, VIEW_ARTICLE_COUNT, Sort.Direction.DESC, ID);
        Page<Article> page = articleRepository.findAll(pageRequest.of());
        log.info("page : {} ", page.getContent());
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
    }

    public Article save(ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        User user = userService.getUserByEmail(userResponseDto.getEmail());
        Article article = ArticleConverter.toEntity(articleRequestDto, user);

        articleRepository.save(article);
        return article;
    }

    public Article update(long articleId, ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        Article currentArticle = findArticle(articleId);
        Article changedArticle = ArticleConverter.toEntity(articleRequestDto, currentArticle.getAuthor());
        User user = userService.getUserByEmail(userResponseDto.getEmail());

        checkAuthentication(user.getId(), userResponseDto);
        currentArticle.update(changedArticle, user);

        return currentArticle;
    }

    public void delete(long articleId, UserResponseDto userResponseDto) {
        checkAuthentication(articleId, userResponseDto);
        articleRepository.deleteById(articleId);
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return article.getComments();
    }

    public void checkAuthentication(Long articleId, UserResponseDto userResponseDto) {
        User user = userService.getUserByEmail(userResponseDto.getEmail());
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        log.debug("{} article.getAuthor().getEmail() >> {}", LOG_TAG, article.getAuthor().getEmail());

        if (!article.isAuthor(user)) {
            throw new NotFoundArticleException();
        }
    }
}