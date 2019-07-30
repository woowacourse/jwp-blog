package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchAuthorException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Long save(UserDto.Response userDto, ArticleDto.Create articleDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);
        Article newArticle = articleDto.toArticle(user);
        return articleRepository.save(newArticle).getId();
    }

    public List<ArticleDto.Response> findAll() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleDto.Response.class))
                .collect(Collectors.toList());
    }

    public ArticleDto.Response findById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return modelMapper.map(article, ArticleDto.Response.class);
    }

    public ArticleDto.Response findById(UserDto.Response userDto, Long articleId) {
        checkAuthor(userDto, articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return modelMapper.map(article, ArticleDto.Response.class);
    }

    public Long update(UserDto.Response userDto, Long articleId, ArticleDto.Update articleDto) {
        checkAuthor(userDto, articleId);
        Article updatedArticle = articleDto.toArticle(articleId);
        return articleRepository.save(updatedArticle).getId();
    }

    public void deleteById(UserDto.Response userDto, Long articleId) {
        checkAuthor(userDto, articleId);
        articleRepository.deleteById(articleId);
    }

    private void checkAuthor(UserDto.Response userDto, Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);

        if (!article.isWrittenBy(user)) {
            throw new NotMatchAuthorException();
        }
    }
}
