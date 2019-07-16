package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.exception.ArticleDtoNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        Article article = articleRepository.findById(id);
        return assembler.convertToDto(article);
    }

    public ArticleDto save(ArticleDto articleDTO) {
        checkDto(articleDTO);
        articleDTO.setId(articleRepository.getLatestId());
        Article article = assembler.convertToEntity(articleDTO);
        Article persistArticle = articleRepository.save(article);
        return assembler.convertToDto(persistArticle);
    }

    public void update(final int id, final ArticleDto articleDTO) {
        checkDto(articleDTO);
        Article article = assembler.convertToEntity(articleDTO);
        articleRepository.update(id, article);
    }

    private void checkDto(ArticleDto articleDTO) {
        if (Objects.isNull(articleDTO)) {
            throw new ArticleDtoNotFoundException();
        }
    }

    public void delete(final int id) {
        articleRepository.delete(id);
    }
}
