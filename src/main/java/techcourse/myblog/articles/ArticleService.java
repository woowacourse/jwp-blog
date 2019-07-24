package techcourse.myblog.articles;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find Article : " + id));
    }

    public Article edit(Article editedArticle) {
        Article article = findById(editedArticle.getId());

        article.update(editedArticle);

        return article;
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }
}
