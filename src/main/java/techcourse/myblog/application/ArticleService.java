package techcourse.myblog.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ArticleService(ArticleRepository articleRepository, UserService userService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    public List<Article> findAll() {
        return Collections.unmodifiableList(articleRepository.findAll());
    }

    public Long save(ArticleDto articleDto, UserResponse userResponse) {
        User author = userService.findById(userResponse.getId());

        Article article = modelMapper.map(articleDto, Article.class);
        article.setAuthor(author);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
    }

    public Article findByUser(Long articleId, UserResponse userResponse) {
        Article article = findById(articleId);
        User user = modelMapper.map(userResponse, User.class);

        if (!article.isSameAuthor(user)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }

        return article;
    }

    @Transactional
    public ArticleDto modify(ArticleDto articleDto, Long articleId, UserResponse userResponse) {
        Article article = findById(articleId);
        User author = userService.findById(userResponse.getId());

        if (!article.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }

        article.updateArticle(modelMapper.map(articleDto, Article.class));
        articleDto.setId(articleId);
        return articleDto;
    }

    public void remove(Long articleId, UserResponse userResponse) {
        checkAuthenticatedAuthor(articleId, userResponse);
        articleRepository.deleteById(articleId);
    }

    private void checkAuthenticatedAuthor(Long articleId, UserResponse userResponse) {
        Article article = findById(articleId);
        User author = userService.findById(userResponse.getId());

        if (!article.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }
    }
}
