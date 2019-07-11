package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public void add(Article article) {
        article.setId(issuedId());
        articles.add(article);
    }

    private int issuedId() {
        int size = articles.size();
        if (size == 0) {
            return 0;
        }
        return articles.get(size - 1).getId() + 1;
    }

    public Article findArticleById(int id) {
        return articles.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("해당하는 ID의 기사가 없습니다."));
    }

    //TODO 삭제하고 새로 추가하는게 나은가 아니면 수정하는게 더 나을까?
    public void updateArticle(int id, Article article) {
        int index = 0;
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getId() == id) {
                index = i;
            }
        }
        articles.set(index, article);
    }

    public void deleteById(int id) {
        articles.remove(findArticleById(id));
    }
}
