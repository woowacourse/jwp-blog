package techcourse.myblog.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import techcourse.myblog.domain.Article;

@Repository
public class ArticleRepository {
	private static int articleCount = 0;
	private List<Article> articles = new ArrayList<>();

	public List<Article> findAll() {
		return articles;
	}

	public Article save(Article article) {
		article.setId(++articleCount);
		articles.add(article);
		return findById(articleCount);
	}

	public Article findById(int id) {
		return articles.stream()
				.filter(article -> article.getId() == id)
				.findFirst()
				.orElseThrow(IllegalArgumentException::new)
				;
	}

	public void update(Article modifiedArticle) {
		Article originArticle = findById(modifiedArticle.getId());
		originArticle.setCoverUrl(modifiedArticle.getCoverUrl());
		originArticle.setTitle(modifiedArticle.getTitle());
		originArticle.setContents(modifiedArticle.getContents());
	}

	public void deleteById(int articleId) {
		Article article = findById(articleId);
		articles.remove(article);
	}
}
