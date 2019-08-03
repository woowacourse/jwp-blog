package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.exception.NoPermissionArticleException;
import techcourse.myblog.service.exception.NoRowException;
import techcourse.myblog.web.dto.ArticleDto;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NoRowException::new);
    }

    public Article save(ArticleDto articleDto, User user) {
        return articleRepository.save(articleDto.create(user));
    }

    public Article update(Long articleId, ArticleDto articleDto, User loginUser) {
        exist(articleId, loginUser);
        Article selectedArticle = findById(articleId);
        return articleRepository.save(selectedArticle.update(articleDto));
    }

    public void delete(Long articleId, User loginUser) {
        exist(articleId, loginUser);
        articleRepository.deleteById(articleId);
    }

    public void exist(Long articleId, User author) {
        if (!articleRepository.existsByIdAndAuthor(articleId, author)) {
            throw new NoPermissionArticleException("게시글에 대한 권한이 없습니다.");
        }
    }
}
