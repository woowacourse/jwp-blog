package techcourse.myblog.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.assembler.ArticleAssembler;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchArticleAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final UserService userService;

    private final ArticleAssembler articleAssembler = ArticleAssembler.getInstance();

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Transactional
    public Long save(ArticleDto articleDto, User user) {
        User author = userService.findUserById(user.getId());
        Article article = new Article.ArticleBuilder()
                .contents(articleDto.getContents())
                .title(articleDto.getTitle())
                .coverUrl(articleDto.getCoverUrl())
                .author(author)
                .build();

        log.info(article.toString());
        return articleRepository.save(article).getId();
    }

    Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(Long articleId) {
        Article article = findArticleById(articleId);
        return articleAssembler.convertEntityToDto(article);
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
        return articleRepository.findAll().stream()
                .map(articleAssembler::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public void matchAuthor(Long articleId, User user) {
        User author = findArticleById(articleId).getAuthor();
        if (!author.equals(user)) {
            throw new NotMatchArticleAuthorException("너는 이 글에 작성자가 아니다. 꺼져라!");
        }
    }

    public boolean matchAuthor(ArticleDto articleDto, User user) {
        return articleDto.getAuthor().match(user);
    }
}