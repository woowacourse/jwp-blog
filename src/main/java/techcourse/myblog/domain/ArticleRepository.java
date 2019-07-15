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
        int id = nextId();
        article.setId(id);

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
