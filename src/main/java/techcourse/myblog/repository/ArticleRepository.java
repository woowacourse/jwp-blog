package techcourse.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Transactional
    @Modifying
    @Query("update Article a set a.title = ?1, a.coverUrl = ?2, a.contents = ?3 where a.id = ?4")
    int updateArticleById(String title, String coverUrl, String contents, Long id);

}
