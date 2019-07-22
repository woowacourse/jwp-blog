package techcourse.myblog.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

import javafx.scene.control.Pagination;
import techcourse.myblog.domain.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

}
