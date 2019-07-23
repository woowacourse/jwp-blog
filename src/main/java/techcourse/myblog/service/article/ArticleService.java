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

    public ArticleDto findById(final int id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            return convertToDto(article.get());
        }
        throw new ArticleNotFoundException();
    }

    public int save(ArticleDto articleDTO) {
        checkDto(articleDTO);
        Article article = convertToEntity(articleDTO);
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    @Transactional
    public void update(final int id, final ArticleDto articleDTO) {
        checkDto(articleDTO);
        Article article = convertToEntity(articleDTO);
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Article articleToUpdate = articleOptional.get();
            articleToUpdate.update(article);
            articleRepository.save(articleToUpdate);
        }
    }

    private void checkDto(final ArticleDto articleDTO) {
        if (Objects.isNull(articleDTO)) {
            throw new ArticleDtoNotFoundException();
        }
    }

    public void delete(final int id) {
        articleRepository.deleteById(id);
    }
}
