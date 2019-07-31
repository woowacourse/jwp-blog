package techcourse.myblog.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.exception.NoArticleException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleService(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Long post(ArticleDto articleDto) {
        Article article = modelMapper.map(articleDto, Article.class);
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public Article findById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
    }

    @Transactional
    public void editArticle(ArticleDto articleDto, long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoArticleException("해당 게시물은 존재하지 않습니다!"));
        article.updateArticle(modelMapper.map(articleDto, Article.class));
    }

    public void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
