package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.User;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    void deleteByUser(User user);
}
