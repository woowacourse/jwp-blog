package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    //TODO 생성자를 안만들고 이렇게 하는 것 도 좋은가요?
    //ans : 동작하는 코드에 차이는 없어서 괜찮다.
    private List<Article> articles = new ArrayList<>();
    private int id = 0;

    //TODO 이렇게 여기서 new 를해서 새로운 것을 해도 괜찮을까?
    //ans : 상관없다.
    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    //DB에서 autoIncrement가 1부터 시작한다.
    //그런거 고려해보자.
    public void save(Article article) {
        article.setId(id++);
        articles.add(article);
    }

    //참고로 레포에는 디비 명령어를 직접적으로 안쓰는 추세(거의 확저잉다) 레포는 디비와
    //연계성을 제거한다는 이유로 find save modify remove라는 메서드명을 많이 사용한다.
    public Article findArticleById(int id) {
        return articles.stream()
                .filter(x -> x.checkId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 ID의 기사가 없습니다."));
    }

    //TODO 삭제하고 새로 추가하는게 나은가 아니면 수정하는게 더 나을까? + Article의 불변성..?
    //Value Object는 delete insert를 한다. \
    //식별성을 가진(Id) 같은거는 Update를 많이한다. 아니면 id값이 훼손될 수도 있다.
    //delete insert 중에 끊길수도 있고 하니깐....
    //update가 과연 필요한가 ? 그냥 가져와서 setter등을 사용해서 바꾸면 되지 않을까?
    public void updateArticle(int id, Article article) {
        int index = 0;
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).checkId(id)) {
                index = i;
            }
        }
        articles.set(index, article);
    }

    public void removeById(int id) {
        articles.remove(findArticleById(id));
    }
}
