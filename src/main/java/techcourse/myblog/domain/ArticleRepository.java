package techcourse.myblog.domain;

//import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

//@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {}
