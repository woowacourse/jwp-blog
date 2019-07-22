package techcourse.myblog.dto.request;

import techcourse.myblog.domain.Article;

public class ArticleDto {
	private Long id;
	private String title;
	private String contents;
	private String coverUrl;

	public ArticleDto() {
	}

	//TODO : POST 시 ID가 비어있어서 새로운 생성자를 만든건데 이렇게 두 가지의 생성자를 만드는게 바람직한 일인가?
	public ArticleDto(String title, String contents, String coverUrl) {
		this.title = title;
		this.contents = contents;
		this.coverUrl = coverUrl;
	}

	public ArticleDto(final Long id, String title, String contents, String coverUrl) {
		this.id = id;
		this.title = title;
		this.contents = contents;
		this.coverUrl = coverUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Article valueOfArticle() {
		return new Article(this.title, this.contents, this.coverUrl);
	}

	public Article valueOfArticle(Long articleId) {
		return new Article(articleId, this.title, this.contents, this.coverUrl);
	}
}
