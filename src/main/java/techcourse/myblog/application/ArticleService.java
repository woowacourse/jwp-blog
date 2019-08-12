package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import techcourse.myblog.application.assembler.ArticleAssembler;
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
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleAssembler articleAssembler;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService,
                          ArticleAssembler articleAssembler) {
        this.userService = userService;
        this.articleRepository = articleRepository;
        this.articleAssembler = articleAssembler;
    }

    public Long save(ArticleDto articleDto, UserResponse userResponse) {
        User author = userService.findById(userResponse.getId());

        Article article = articleAssembler.convertToArticle(articleDto, author);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public ArticleDto find(Long articleId, UserResponse userResponse) {
        return articleAssembler.convertToDto(findByUser(articleId, userResponse));
    }

    public ArticleDto find(Long articleId) {
        return articleAssembler.convertToDto(findById(articleId));
    }

    public List<ArticleDto> findAll() {
        List<ArticleDto> articles = articleRepository.findAll().stream()
                .map(articleAssembler::convertToDto)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(articles);
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
    }

    private Article findByUser(Long articleId, UserResponse userResponse) {
        Article article = findById(articleId);
        User author = userService.findById(userResponse.getId());

        if (!article.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }

        return article;
    }

    @Transactional
    public ArticleDto modify(ArticleDto articleDto, Long articleId, UserResponse userResponse) {
        Article article = findByUser(articleId, userResponse);
        User author = userService.findById(userResponse.getId());

        article.updateArticle(articleAssembler.convertToArticle(articleDto, author));
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
