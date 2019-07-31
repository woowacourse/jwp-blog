package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.article.ArticleDtos;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.domain.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    public long createArticle(ArticleDto articleDto, long userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        maybeUser.ifPresent(user -> articleDto.setUserDto(UserDto.from(user)));
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
