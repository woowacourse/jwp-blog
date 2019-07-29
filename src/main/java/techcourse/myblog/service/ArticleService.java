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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

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
    public ArticleDto updateByArticle(long articleId, ArticleDto articleDto) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            maybeArticle.get().update(articleDto.toEntity());

            return readById(articleId);
        }
        throw new IllegalArgumentException("업데이트 할 수 없습니다.");
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<ArticleDto> readAll() {
        return new ArticleDtos(articleRepository.findAll()).getArticleDtos();
    }

    public List<ArticleDto> readByCategoryId(long categoryId) {
        return new ArticleDtos(articleRepository.findByCategoryId(categoryId)).getArticleDtos();
    }
}
