package techcourse.myblog.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByAuthor(User author);
}