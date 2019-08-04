package techcourse.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Long save(ArticleDto articleDto, User author) {
        Article newArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), author);
        articleRepository.save(newArticle);
        return newArticle.getId();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public Page<Article> getAllArticles(final Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Transactional
    public Article update(ArticleDto articleDto, User user, Long articleId) {
        checkOwner(articleId, user);
        Article oldArticle = findById(articleId);

        Article updatedArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), user);
        return oldArticle.update(updatedArticle);
    }

    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<Comment> findAllComment(Long articleId) {
        Article foundArticle = findById(articleId);
        return foundArticle.getSortedComments();
    }

    public void checkOwner(Long articleId, User user) {
        Article foundArticle = findById(articleId);
        foundArticle.checkOwner(user);
    }
}
