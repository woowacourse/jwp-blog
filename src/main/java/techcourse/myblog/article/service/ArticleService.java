package techcourse.myblog.article.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.dto.ArticleCreateDto;
import techcourse.myblog.article.dto.ArticleResponseDto;
import techcourse.myblog.article.dto.ArticleUpdateDto;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ArticleService(ArticleRepository articleRepository, UserService userService, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<ArticleResponseDto> findAll() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    public ArticleResponseDto save(ArticleCreateDto articleDto, long authorId) {
        User author = userService.findById(authorId);
        Article newArticle = articleDto.toArticle(author);
        return modelMapper.map(articleRepository.save(newArticle), ArticleResponseDto.class);
    }

    public Article findById(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
    }

    public ArticleResponseDto find(long articleId, long authorId) {
        Article article = findById(articleId);
        if (article.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        return modelMapper.map(article, ArticleResponseDto.class);
    }

    public ArticleResponseDto find(long articleId) {
        Article article = findById(articleId);
        return modelMapper.map(article, ArticleResponseDto.class);
    }

    public ArticleResponseDto update(long articleId, ArticleUpdateDto articleDto, long authorId) {
        Article article = checkAuthority(articleId, authorId);
        return modelMapper.map(article.update(articleDto), ArticleResponseDto.class);
    }

    public void deleteById(long articleId, long authorId) {
        Article article = checkAuthority(articleId, authorId);
        articleRepository.delete(article);
    }

    private Article checkAuthority(long articleId, long authorId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        if (article.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        return article;
    }
}
