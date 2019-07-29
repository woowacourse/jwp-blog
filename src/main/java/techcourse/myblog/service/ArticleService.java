package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchAuthorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);
        if (!article.isWrittenBy(user)) {
            throw new NotMatchAuthorException();
        }
        return modelMapper.map(article, ArticleDto.Response.class);
    }

    public Long update(UserDto.Response userDto, Long articleId, ArticleDto.Update articleDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);

        if (!article.isWrittenBy(user)) {
            throw new NotMatchAuthorException();
        }

        Article updatedArticle = articleDto.toArticle(article.getId());
        return articleRepository.save(updatedArticle).getId();
    }

    public void deleteById(UserDto.Response userDto, Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);

        if (!article.isWrittenBy(user)) {
            throw new NotMatchAuthorException();
        }

        articleRepository.deleteById(articleId);
    }

    public Long save(UserDto.Response userDto, ArticleDto.Create articleDto) {
        User user = userRepository.findById(userDto.getId())
            .orElseThrow(NotFoundUserException::new);
        Article newArticle = articleDto.toArticle(user);
        return articleRepository.save(newArticle).getId();
    }
}
