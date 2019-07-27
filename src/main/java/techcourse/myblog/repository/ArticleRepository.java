package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techcourse.myblog.model.Article;

import java.util.ArrayList;
import java.util.List;

public interface ArticleRepository  extends JpaRepository<Article, Long> {
}
