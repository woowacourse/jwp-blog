package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.utils.converter.DtoConverter;
import techcourse.myblog.utils.page.PageRequest;

import java.util.List;

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

    public List<Article> findAll() {
        PageRequest pageRequest = new PageRequest(START_PAGE, VIEW_ARTICLE_COUNT, Sort.Direction.DESC, ID);
        Page<Article> page = articleRepository.findAll(pageRequest.of());
        log.info("page : {} ", page.getContent());
        return page.getContent();
    }

    public Article findArticle(long articleId) {
        return getArticle(articleId);
    }

    private Article getArticle(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleException::new);
    }

    @Transactional
    public Article save(ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        User user = userService.getUserByEmail(userResponseDto);
        Article article = DtoConverter.convert(articleRequestDto, user);
        articleRepository.save(article);
        return article;
    }

    @Transactional
    public Article update(long articleId, ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        Article originArticle = findArticle(articleId);
        User author = userService.getUserByEmail(userResponseDto);
        originArticle.update(DtoConverter.convert(articleRequestDto, author));
        return originArticle;
    }

    public void delete(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<Comment> getComments(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleException::new);
        return article.getComments();
    }
}