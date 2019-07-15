package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    //식별성이 있는건 가변
    //없는건 불변이다.
    //articel은 값 객체가 아니다.
    //ID마다 서로 다른 객체라서.
    //무수히 많은 애들이 생길거라서,
    //보통은 이런 애들은 Entity라고 부른다.
    //그리고 Entity는 이렇게 final이 아니고 바끼는게 당연하다
    //setter 대신에 메서드이름은 change로 바꾸는.....
    //set, chage라는 어감에 대해 느껴보자
    //chage는 기존에 있는데 바꾸는 어감이고 set은 처음에 없어도 주입(?)을 하는 그런 느낌이다.
    //id는 초기에 빌 수 있으니깐 set도 괜찮지 않을까?

    private int id;
    private final String title;
    private final String coverUrl;
    private final String contents;

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public boolean checkId(int id) {
        return this.id == id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
