package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.*;
import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.ArticleResponse;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.exception.ArticleException;
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
        Article originArticle = findArticleById(articleId);
        User author = userService.getUserByEmail(userResponse);
        originArticle.update(ArticleAssembler.toEntity(articleRequest, author));
    }

    public void delete(long articleId) {
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