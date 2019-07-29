//package techcourse.myblog.jpaexercise;
//
//import javax.persistence.*;
//
//@Entity
//public class Article {
//
//    @Id
//    @GeneratedValue
//    private long id;
//
//    @Column(nullable = false)
//    private String title;
//
//    private String coverUrl;
//
//    @Column(length = 1000)
//    private String contents;
//
//    @ManyToOne // 양방향은 정말 필요할 때에만
//    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_article_to_user"))
//    private User author;
//
//    public Article() {}
//
//    public long getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(final String title) {
//        this.title = title;
//    }
//
//    public String getContents() {
//        return contents;
//    }
//
//    public void setContents(final String contents) {
//        this.contents = contents;
//    }
//
//    public String getCoverUrl() {
//        return coverUrl;
//    }
//
//    public void setCoverUrl(final String coverUrl) {
//        this.coverUrl = coverUrl;
//    }
//
//    public User getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(final User author) {
//        this.author = author;
//    }
//}