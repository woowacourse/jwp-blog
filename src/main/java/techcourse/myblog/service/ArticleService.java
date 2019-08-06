package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDetails;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchAuthorException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Long save(UserDto.Response userDto, ArticleDetails articleDetails) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);
        Article article = new Article(articleDetails, user);
        return articleRepository.save(article).getId();
    }

    public List<ArticleDto> findAll() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleDto.class))
                .collect(Collectors.toList());
    }

    public ArticleDto findById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return modelMapper.map(article, ArticleDto.class);
    }

    public ArticleDto findById(UserDto.Response userDto, Long articleId) {
        checkAuthor(userDto, articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return modelMapper.map(article, ArticleDto.class);
    }

    @Transactional
    public ArticleDto update(UserDto.Response userDto, Long articleId, ArticleDetails articleDetails) {
        checkAuthor(userDto, articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        article.update(articleDetails);
        return modelMapper.map(article, ArticleDto.class);
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
