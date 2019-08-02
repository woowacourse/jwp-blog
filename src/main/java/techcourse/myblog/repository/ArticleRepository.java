package techcourse.myblog.repository;

import techcourse.myblog.article.Article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
