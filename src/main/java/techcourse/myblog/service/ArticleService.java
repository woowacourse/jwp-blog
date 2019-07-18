package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleDtoNotFoundException;
import techcourse.myblog.exception.ArticleNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleAssembler assembler;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        assembler = new ArticleAssembler();
    }

    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        return Collections.unmodifiableList(articles.stream()
                .map(assembler::convertToDto)
                .collect(Collectors.toList()));
    }

    public ArticleDto findById(final int id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            return assembler.convertToDto(article.get());
        }
        throw new ArticleNotFoundException();
    }

    public int save(ArticleDto articleDTO) {
        checkDto(articleDTO);
        Article article = assembler.convertToEntity(articleDTO);
        Article persistArticle = articleRepository.save(article);
        return persistArticle.getId();
    }

    @Transactional
    public void update(final int id, final ArticleDto articleDTO) {
        checkDto(articleDTO);
        Article article = assembler.convertToEntity(articleDTO);
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Article articleToUpdate = articleOptional.get();
            articleToUpdate.update(article);
            articleRepository.save(articleToUpdate);
        }
    }

    private void checkDto(ArticleDto articleDTO) {
        if (Objects.isNull(articleDTO)) {
            throw new ArticleDtoNotFoundException();
        }
    }

    public void delete(final int id) {
        articleRepository.deleteById(id);
    }
}
