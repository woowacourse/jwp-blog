package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class ArticleRepository {
    private final AtomicInteger newArticleId = new AtomicInteger(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<Integer, Article> articles = new TreeMap<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    public int insertArticle(Article article) {
        // '새로운 id를 생성하는' 책임을 여기서 하는게 좋은걸까? (즉... id 에 대한 책임???)
        // id 가 db 같은 저장소에서만 사용될 용도이면... 여기에 저장하는게 맞는 것도 같고...
        // 그런데 한 편으로는 요청 내에도 id 가 포함되기 때문에... 밖에서도 보이는 값이기도 하다.
        // 그런데 id 를 외부에서 넣어 줄 일은 없다..
        // 그저 클릭 등으로 ... 내가 그려준.. 즉 response 로 활용된 정보에서만 쓰일 뿐...
        // request 로 들어오는 모든 id 는 uri를 타고 넘어오기 (Rest API 를 따른다면?) 때문에...
        // requestDto 에는 존재하지 않는게 맞는 듯하고...
        // responseDto 에는 존재하는게 맞는 듯...
        //
        // 그렇다면... id 를 생성하고 관리하는 부분은.. db 혹은 여기가 맞는 듯하구만
        // 지금은 in-memory Repository 이기 때문에 여기서 id 생성을 담당하자
        int id = nextId();
        article = article.replaceId(id);

        lock.writeLock().lock();
        articles.put(id, article);
        lock.writeLock().unlock();

        return id;
    }

    private int nextId() {
        return newArticleId.getAndIncrement();
    }

    public Article findById(int articleId) {
        lock.readLock().lock();
        Article article = articles.get(articleId);
        lock.readLock().unlock();

        return article;
    }

    public void updateArticle(Article article) {
        lock.writeLock().lock();
        articles.put(article.getId(), article);
        lock.writeLock().unlock();
    }

    public void deleteArticle(int articleId) {
        lock.writeLock().lock();
        articles.remove(articleId);
        lock.writeLock().unlock();
    }
}
