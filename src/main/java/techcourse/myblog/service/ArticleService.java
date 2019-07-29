package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.service.assembler.ArticleAssembler;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.exception.NoArticleException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private final ArticleAssembler articleAssembler;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.articleAssembler = ArticleAssembler.getInstance();
    }

    public List<ArticleDto> findAll() {
        return articleAssembler.convertToEntities(articleRepository.findAll());
    }

    public Long post(ArticleDto articleDto) {
        Article article = articleAssembler.convertToEntity(articleDto);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public ArticleDto findById(long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
        return articleAssembler.convertToDto(article);
    }

    @Transactional
    public void editArticle(ArticleDto articleDto, long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
        article.updateArticle(articleAssembler.convertToEntity(articleDto));
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
