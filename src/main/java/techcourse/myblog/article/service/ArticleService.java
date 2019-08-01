package techcourse.myblog.article.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.repository.ArticleRepository;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.service.CommentService;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.service.UserService;
import techcourse.myblog.utils.converter.ArticleConverter;
import techcourse.myblog.utils.page.PageRequest;

import java.util.List;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private static final String ID = "id";
    private static final int START_PAGE = 1;
    private static final int VIEW_ARTICLE_COUNT = 10;

    private final UserService userService;
    private final CommentService commentService;
    private final ArticleRepository articleRepository;

    public ArticleService(UserService userService, CommentService commentService, ArticleRepository articleRepository) {
        this.userService = userService;
        this.commentService = commentService;
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

    @Transactional
    public Article save(ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        User user = userService.getUserByEmail(userResponseDto.getEmail());
        Article article = ArticleConverter.convert(articleRequestDto, user);
        articleRepository.save(article);
        return article;
    }

    @Transactional
    public Article update(long articleId, ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        Article originArticle = findArticle(articleId);
        User user = userService.getUserByEmail(userResponseDto.getEmail());

        originArticle.update(ArticleConverter.convert(articleRequestDto, originArticle.getAuthor()), user);
        return originArticle;
    }

    @Transactional
    public void delete(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        commentService.removeAllByArticle(article);
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public List<Comment> getCommentsByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return article.getComments();
    }

    public void checkAuthentication(Long articleId, UserResponseDto userResponseDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);

        if (!article.getAuthor().getEmail().equals(userResponseDto.getEmail())) {
            throw new NotFoundArticleException();
        }
    }
}