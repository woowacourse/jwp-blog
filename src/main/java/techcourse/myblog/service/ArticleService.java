package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(Article article){
        articleRepository.save(article);
        return article;
    }

    public Article findById(long id){
        return articleRepository.findById(id).get();
    }

    public Iterable<Article> findAll(){
        return articleRepository.findAll();
    }

    public Article update(Article article) {
        articleRepository.save(article);
        return article;
    }

    public void delete(Long id){
        articleRepository.deleteById(id);
    }
}
