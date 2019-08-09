package techcourse.myblog.service;

import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleGenericService {
    private static final String NOT_EXIST_ARTICLE = "해당 아티클이 없습니다.";
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArticleGenericService(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public <T> List<T> findAll(Class<T> type) {
        return articleRepository.findAll().stream()
                .map(article -> modelMapper.map(article, type))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public <T> List<T> findAllPage(Pageable pageable, Class<T> type) {
        Page<Article> page = articleRepository.findAll(pageable);
        return page.getContent().stream()
                .map(article -> modelMapper.map(article, type))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public <T> T findArticle(long articleId, Class<T> type) {
        return modelMapper.map(articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(NOT_EXIST_ARTICLE)), type);
    }

    @Transactional
    public <T> T add(ArticleDto articleDto, User author, Class<T> type) {
        User user = userRepository.getOne(author.getId());
        Article article = articleDto.toEntity(user);
        return modelMapper.map(articleRepository.save(article), type);
    }

    public <T> T update(long articleId, ArticleDto articleDto, User author, Class<T> type) {
        User user = userRepository.findByEmailEmail(author.getEmail()).orElseThrow(UserException::new);
        Article originArticle = findArticle(articleId, Article.class);
        originArticle.update(articleDto.toEntity(user));
        return modelMapper.map(originArticle, type);
    }

    public void delete(long articleId, User author) {
        Article article = findArticle(articleId, Article.class);
        if (article.isAuthor(author)) {
            articleRepository.deleteById(articleId);
            return;
        }
        throw new UserException();
    }
}
