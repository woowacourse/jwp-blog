package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Long save(ArticleDto articleDto) {
        Article newArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), articleDto.getAuthor());
        articleRepository.save(newArticle);
        return newArticle.getId();
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Transactional
    public Article update(ArticleDto articleDto) {
        Article oldArticle = articleRepository.findById(articleDto.getId()).orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));
        Article updatedArticle =  new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), articleDto.getAuthor());
        return oldArticle.update(updatedArticle);
    }
}
