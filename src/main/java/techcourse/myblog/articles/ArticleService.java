package techcourse.myblog.articles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find Article : " + id));
    }

    public Article edit(Article editedArticle) {
        Article article = findById(editedArticle.getId());

        article.update(editedArticle.getTitle(), editedArticle.getCoverUrl(), editedArticle.getContents());

        return articleRepository.save(article);
    }
}
