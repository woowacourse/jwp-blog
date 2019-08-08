package techcourse.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private static final String NOT_EXIST_ARTICLE = "해당 아티클이 없습니다.";
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Article> findAllPage(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(pageable);
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public Article findArticle(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(NOT_EXIST_ARTICLE));
    }

    @Transactional
    public Article add(ArticleDto articleDto, User author) {
        User user = userRepository.getOne(author.getId());
        Article article = articleDto.toEntity(user);
        return articleRepository.save(article);
    }

    public Article update(long articleId, ArticleDto articleDto, User author) {
        User user = userRepository.findByEmailEmail(author.getEmail()).orElseThrow(UserException::new);
        Article originArticle = findArticle(articleId);
        originArticle.update(articleDto.toEntity(user));
        return originArticle;
    }

    public void delete(long articleId, User author) {
        Article article = findArticle(articleId);
        if (article.isAuthor(author)) {
            articleRepository.deleteById(articleId);
            return;
        }
        throw new UserException();
    }
}