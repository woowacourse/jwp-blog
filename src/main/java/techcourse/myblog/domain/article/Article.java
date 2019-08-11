package techcourse.myblog.domain.article;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.domain.BaseEntity;
import techcourse.myblog.domain.article.exception.MismatchArticleAuthorException;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@DynamicUpdate
public class Article extends BaseEntity {
    private static final String FK_FIELD_NAME = "author";
    private static final String FK_NAME = "fk_article_to_user";

    @Embedded
    private ArticleFeature articleFeature;

    @ManyToOne
    @JoinColumn(name = FK_FIELD_NAME, foreignKey = @ForeignKey(name = FK_NAME))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
    
    public Article(ArticleFeature articleFeature, User user) {
        this.articleFeature = articleFeature;
        this.author = user;
    }

    public void update(Article article) {
        validateAuthor(article);
        this.articleFeature = article.articleFeature;
    }

    public void validateAuthor(User user) {
        if (matchAuthor(user)) {
            return;
        }
        throw new MismatchArticleAuthorException();
    }

    private void validateAuthor(Article article) {
        if (matchAuthor(article.author)) {
            return;
        }
        throw new MismatchArticleAuthorException();
    }

    private boolean matchAuthor(User user) {
        return this.author.equals(user);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", articleFeature='" + articleFeature + '\'' +
                ", author=" + author +
                ", createTimeAt=" + createTimeAt +
                ", updateTimeAt=" + updateTimeAt +
                '}';
    }
}
