package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleResponseDto;
import techcourse.myblog.application.exception.NotFoundArticleException;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.user.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ArticleReadService {
    private final ArticleRepository articleRepository;

    public ArticleReadService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleResponseDto> findAll() {
        return Collections.unmodifiableList(
                articleRepository.findAll().stream()
                        .map(ArticleAssembler::buildArticleResponseDto)
                        .collect(Collectors.toList())
        );
    }

    public Article findByIdWithArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
    }

    public ArticleResponseDto findById(Long articleId) {
        return ArticleAssembler.buildArticleResponseDto(findByIdWithArticle(articleId));
    }

    public ArticleResponseDto findByIdAndValidUser(Long articleId, User user) {
        return ArticleAssembler.buildArticleResponseDto(findByIdWithArticle(articleId).validateAuthor(user));
    }
}
