package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.translator.ArticleTranslator;
import techcourse.myblog.translator.ModelTranslator;
import techcourse.myblog.translator.UserTranslator;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelTranslator<Article, ArticleDto> articleTranslator;
    private final ModelTranslator<User, UserDto> userTranslator;

    public ArticleService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.articleTranslator = new ArticleTranslator();
        this.userTranslator = new UserTranslator();
    }

    public Article create(final ArticleDto articleDto) {
        Article article = articleTranslator.toEntity(new Article(), articleDto);
        return articleRepository.save(article);
    }

    public Article create(final ArticleDto articleDto, final UserDto userDto) {
        articleDto.setAuthor(userTranslator.toEntity(new User(), userDto));
        Article article = articleTranslator.toEntity(new Article(), articleDto);
        return articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(final Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("존재하지 않는 게시글 입니다."));
    }

    public Article findById(final Long articleId, final UserDto userDto) {
        Article article = findById(articleId);
        User user = userTranslator.toEntity(new User(), userDto);
        if (user.equals(article.getAuthor())) {
            return article;
        }
        throw new NotMatchAuthenticationException("접근할 수 없는 게시글 입니다.");
    }

    public Article update(final ArticleDto articleDto, final Long articleId, final UserDto userDto) {
        Article article = findById(articleId, userDto);
        Article updateArticle = articleTranslator.toEntity(article, articleDto);
        return articleRepository.save(updateArticle);
    }

    public void delete(final Long articleId, final UserDto userDto) {
        Article article = findById(articleId, userDto);
        articleRepository.delete(article);
    }
}
