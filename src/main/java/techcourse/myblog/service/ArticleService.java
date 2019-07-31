package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.article.ArticleDtos;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.UserDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserService userService, CommentService commentService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    public long createArticle(ArticleDto articleDto, long userId) {
        UserDto userDto = userService.findByUserId(userId);
        articleDto.setUserDto(userDto);
        Article article = articleRepository.save(articleDto.toEntity());
        return article.getId();
    }

    public ArticleDto readById(long articleId) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        return maybeArticle.map(ArticleDto::from)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 읽을 수 없습니다."));
    }

    @Transactional
    public ArticleDto updateByArticle(long articleId, ArticleDto articleDto, long userId) {
        checkAuthor(articleId, userId);
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            maybeArticle.get().update(articleDto.toEntity());

            return readById(articleId);
        }
        throw new IllegalArgumentException("업데이트 할 수 없습니다.");
    }

    @Transactional
    public void deleteById(long articleId, long userId) {
        checkAuthor(articleId, userId);
        commentService.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
    }

    public List<ArticleDto> readAll() {
        return new ArticleDtos(articleRepository.findAll()).getArticleDtos();
    }

    public List<ArticleDto> readByCategoryId(long categoryId) {
        return new ArticleDtos(articleRepository.findByCategoryId(categoryId)).getArticleDtos();
    }

    private void checkAuthor(long articleId, long userId) {
        articleRepository.findById(articleId).ifPresent(article -> {
            if (article.getId() != userId) {
                throw new IllegalArgumentException("허가되지 않은 사용자입니다.");
            }
        });
    }
}
