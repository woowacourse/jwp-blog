package techcourse.myblog.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.ArticleConverter;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

import java.util.List;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleConverter articleConverter;
    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserService userService, ArticleConverter articleConverter) {
        this.articleConverter = articleConverter;
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Transactional
    public Long save(ArticleDto articleDto, String email) {
        User user = userService.findUserByEmail(email);
        Article.ArticleBuilder articleBuilder = new Article.ArticleBuilder();
        Article article = articleBuilder.title(articleDto.getTitle())
                .coverUrl(articleDto.getCoverUrl())
                .contents(articleDto.getContents())
                .author(user)
                .build();
        return articleRepository.save(article).getId();
    }

    @Transactional(readOnly = true)
    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));
    }

    @Transactional
    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void modify(Long articleId, ArticleDto articleDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException(""));

        article.modify(articleConverter.convertFromDto(articleDto));
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        return articleConverter.createFromEntities(articleRepository.findAll());
    }

    public void checkAuthor(Long articleId, String email) {
        log.info("article " + findById(articleId));
        User author = findById(articleId).getAuthor();
        log.info("author: " + author);
        log.info("email: " + email);
        if (!author.isNotMatchEmail(email)) {
            throw new NotMatchAuthorException("너는 이 글에 작성자가 아니다. 꺼져라!");
        }
    }
}