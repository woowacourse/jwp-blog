package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void save(Article article){
        articleRepository.save(article);
    }

    public Article findById(long id){
        return articleRepository.findById(id).get();
    }

    public Iterable<Article> findAll(){
        return articleRepository.findAll();
    }

    public void update(Article article) {
        articleRepository.save(article);
    }

    public void delete(Long id){
        articleRepository.deleteById(id);
    }
}
