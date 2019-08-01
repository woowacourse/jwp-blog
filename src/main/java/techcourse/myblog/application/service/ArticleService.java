package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.ArticleConverter;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleConverter articleConverter = ArticleConverter.getInstance();
    private final UserConverter userConverter = UserConverter.getInstance();
    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Transactional
    public Long save(ArticleDto articleDto, String email) {
        User author = userService.findUserByEmail(email);
        Article article = articleConverter.convertFromDto(articleDto);
        article.init(author);

        return articleRepository.save(article).getId();
    }

    Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        Article article = findArticleById(articleId);
        ArticleDto articleDto = articleConverter.convertFromEntity(article);
        articleDto.setAuthor(userConverter.convertFromEntity(article.getAuthor()));
        return articleDto;
    }

    @Transactional
    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void modify(Long articleId, ArticleDto articleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException(""));
        article.modify(articleDto);
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> articleDtos = articleConverter.createFromEntities(articleRepository.findAll());
        for (int i = 0; i < articleDtos.size(); i++) {
            articleDtos.get(i).setAuthor(userConverter.convertFromEntity(articles.get(i).getAuthor()));
        }
        return articleDtos;
    }

    public void matchAuthor(Long articleId, String email) {
        User author = findArticleById(articleId).getAuthor();
        if (!author.compareEmail(email)) {
            throw new NotMatchAuthorException("너는 이 글에 작성자가 아니다. 꺼져라!");
        }
    }

    public boolean matchAuthor(ArticleDto articleDto, String email) {
        return articleDto.matchEmail(email);
    }
}