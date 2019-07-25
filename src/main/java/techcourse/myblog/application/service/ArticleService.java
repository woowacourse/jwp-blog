package techcourse.myblog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.error.NotFoundArticleIdException;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(ArticleDto articleDto) {
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .coverUrl(articleDto.getCoverUrl())
                .contents(articleDto.getContents())
                .build();
        return articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public Article findById(long id) {
        return articleRepository.findById(id).orElseThrow(() -> new NotFoundArticleIdException("게시글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Iterable<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article update(ArticleDto articleDto, long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundArticleIdException("게시글을 찾을 수 없습니다."));
        article.update(articleDto);
        return article;
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
