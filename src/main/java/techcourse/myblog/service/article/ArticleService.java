package techcourse.myblog.service.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.dto.article.ArticleDto;
import techcourse.myblog.exception.ArticleDtoNotFoundException;
import techcourse.myblog.exception.ArticleNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static techcourse.myblog.service.article.ArticleAssembler.*;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        return Collections.unmodifiableList(articles.stream()
                .map(ArticleAssembler::convertToDto)
                .collect(Collectors.toList()));
    }

    public ArticleDto findById(final Long id) {
        checkNull(id);
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            return convertToDto(article.get());
        }
        throw new ArticleNotFoundException();
    }

    public Long save(final ArticleDto articleDTO) {
        checkNull(articleDTO);
        Article article = convertToEntity(articleDTO);
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    @Transactional
    public void update(final Long id, final ArticleDto articleDTO) {
        checkNull(id);
        checkNull(articleDTO);
        Article article = convertToEntity(articleDTO);
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Article articleToUpdate = articleOptional.get();
            articleToUpdate.update(article);
            articleRepository.save(articleToUpdate);
        }
    }

    private void checkNull(final Long id) {
        if (Objects.isNull(id)) {
            throw new NullPointerException();
        }
    }

    private void checkNull(final ArticleDto articleDTO) {
        if (Objects.isNull(articleDTO)) {
            throw new ArticleDtoNotFoundException();
        }
    }

    public void delete(final Long id) {
        checkNull(id);
        articleRepository.deleteById(id);
    }
}
