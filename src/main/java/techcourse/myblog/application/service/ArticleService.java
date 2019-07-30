package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long save(ArticleDto articleDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        Article article = new Article(articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents(),
                user);
        return articleRepository.save(article).getId();
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));

        return ArticleDto.of(article);
    }

    @Transactional
    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void modify(Long articleId, ArticleDto articleDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException(""));

        article.modify(new Article(articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents(),
                user));
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void checkAuthor(Long articleId, String email) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new);
        article.checkAuthor(email);
    }

    @Transactional
    public UserDto findAuthor(long articleId) {
        User user = articleRepository.findById(articleId)
                .orElseThrow(IllegalArgumentException::new)
                .getUser();
        return new UserDto(user.getEmail(),
                user.getName(),
                user.getPassword());
    }
}