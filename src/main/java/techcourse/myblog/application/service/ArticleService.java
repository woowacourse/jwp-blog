package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchEmailException;
import techcourse.myblog.domain.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Transactional
    public Long save(ArticleDto articleDto, String email) {
        User user = userService.findUserByEmail(email);
        Article article = new Article(articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents(),
                user);

        return articleRepository.save(article).getId();
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        return ArticleDto.of(findArticleById(articleId));
    }

    @Transactional(readOnly = true)
    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("해당 게시물을 찾을 수 없습니다."));
    }

    @Transactional
    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void modify(Long articleId, ArticleDto articleDto, String email) {
        User user = userService.findUserByEmail(email);
        Article article = findArticleById(articleId);

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

    @Transactional(readOnly = true)
    public void checkAuthor(Long articleId, String email) {
        Article article = findArticleById(articleId);
        if (!article.isSameAuthorEmail(email)) {
            throw new NotMatchEmailException("작성자가 다릅니다.");
        }
    }

    @Transactional
    public UserDto findAuthor(long articleId) {
        User user = findArticleById(articleId).getUser();

        return new UserDto(user.getEmail(),
                user.getName(),
                user.getPassword());
    }
}