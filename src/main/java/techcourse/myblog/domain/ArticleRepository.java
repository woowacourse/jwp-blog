package techcourse.myblog.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE Article as a set a.title = :#{#article.title}, a.coverUrl = :#{#article.coverUrl}"
            + ", a.contents = :#{#article.contents} where a.id = :#{#article.id}")
    Integer update(@Param("article") Article article);
}
