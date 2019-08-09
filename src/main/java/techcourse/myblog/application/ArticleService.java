package techcourse.myblog.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.NoArticleException;
import techcourse.myblog.application.exception.NoUserException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Long post(ArticleDto articleDto, UserResponse userResponse) {
        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        Article article = modelMapper.map(articleDto, Article.class);
        article.setAuthor(author);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
    }

    public Article findArticleWrittenByUser(Long articleId, UserResponse userResponse) {
        Article article = findArticleById(articleId);
        User user = modelMapper.map(userResponse, User.class);

        if (!article.isSameAuthor(user)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }

        return article;
    }

    @Transactional
    public void editArticle(ArticleDto articleDto, Long articleId, UserResponse userResponse) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));

        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        if (!article.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }

        article.updateArticle(modelMapper.map(articleDto, Article.class));
    }

    public void deleteById(Long articleId, UserResponse userResponse) {
        checkAuthenticatedAuthor(articleId, userResponse);

        articleRepository.deleteById(articleId);
    }

    private void checkAuthenticatedAuthor(Long articleId, UserResponse userResponse) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));

        User author = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new NoUserException("유저가 존재하지 않습니다."));

        if (!article.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 글을 수정할 수 있습니다.");
        }
    }
}
