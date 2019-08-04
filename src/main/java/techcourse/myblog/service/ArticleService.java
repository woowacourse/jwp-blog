package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleAssembler;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentAssembler;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.article.ArticleRequest;
import techcourse.myblog.dto.article.ArticleResponse;
import techcourse.myblog.dto.comment.CommentResponse;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.exception.article.ArticleAuthenticationException;
import techcourse.myblog.exception.article.ArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.utils.page.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private static final String ID = "id";
    private static final int START_PAGE = 1;
    private static final int VIEW_ARTICLE_COUNT = 10;

    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    public List<ArticleResponse> getArticles() {
        PageRequest pageRequest = new PageRequest(START_PAGE, VIEW_ARTICLE_COUNT, Sort.Direction.DESC, ID);
        List<Article> articles = articleRepository.findAll(pageRequest.of()).getContent();
        log.info("page : {} ", articles);
        return articles.stream()
                .map(ArticleAssembler::toDto)
                .collect(Collectors.toList());
    }

    public ArticleResponse getArticle(long articleId) {
        return ArticleAssembler.toDto(findArticleById(articleId));
    }

    Article findArticleById(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleException::new);
    }

    @Transactional
    public ArticleResponse save(ArticleRequest articleRequest, UserResponse userResponse) {
        User author = userService.getUserByEmail(userResponse);
        Article article = ArticleAssembler.toEntity(articleRequest, author);
        articleRepository.save(article);
        return ArticleAssembler.toDto(article);
    }

    @Transactional
    public void update(long articleId, ArticleRequest articleRequest, UserResponse userResponse) {
        Article article = findArticleById(articleId);
        User user = userService.getUserByEmail(userResponse);

        checkAuthentication(article, userResponse);
        article.update(ArticleAssembler.toEntity(articleRequest, user));
    }

    private void checkAuthentication(Article article, UserResponse userResponse) {
        String articleAuthorEmail = article.getAuthor().getEmail();
        String loggedInUserEmail = userResponse.getEmail();

        if (!articleAuthorEmail.equals(loggedInUserEmail)) {
            throw new ArticleAuthenticationException();
        }
    }

    public void delete(long articleId, UserResponse userResponse) {
        Article article = findArticleById(articleId);
        checkAuthentication(article, userResponse);

        articleRepository.deleteById(articleId);
    }

    public List<CommentResponse> getComments(long articleId) {
        Article article = findArticleById(articleId);
        List<Comment> comments = article.getComments();

        return comments.stream()
                .map(CommentAssembler::toDto)
                .collect(Collectors.toList());
    }
}