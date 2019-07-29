package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

@Service
@Transactional
public class ArticleWriteService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleWriteService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void removeById(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void update(Long articleId, ArticleDto articleDto) {
        findByIdAndAuthor(articleId, articleDto.getAuthor()).update(articleDto.toArticle());
    }

    public Article findByIdAndAuthor(Long articleId, User user) {
        return articleRepository.findByIdAndAuthor(articleId, user)
                .orElseThrow(() -> new MismatchAuthorException("작성자만 접근할 수 있습니다."));
    }
}
