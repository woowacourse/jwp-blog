package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    //TODO 생성자를 안만들고 이렇게 하는 것 도 좋은가요?
    private List<Article> articles = new ArrayList<>();
    private int id = 0;

    //TODO 이렇게 여기서 new 를해서 새로운 것을 해도 괜찮을까?
    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public void save(Article article) {
        article.setId(id++);
        articles.add(article);
    }

    public Article findArticleById(int id) {
        return articles.stream()
                .filter(x -> x.checkId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 ID의 기사가 없습니다."));
    }

    //TODO 삭제하고 새로 추가하는게 나은가 아니면 수정하는게 더 나을까? + Article의 불변성..?
    public void updateArticle(int id, Article article) {
        int index = 0;
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).checkId(id)) {
                index = i;
            }
        }
        articles.set(index, article);
    }

    public void deleteById(int id) {
        articles.remove(findArticleById(id));
    }
}
