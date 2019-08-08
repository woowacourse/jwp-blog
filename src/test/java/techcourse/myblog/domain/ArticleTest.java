package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.service.exception.MismatchAuthorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleTest {
    private User author = new User("author", "author@mail.com", "Passw0rd!");
    private Article originalArticle = new Article("원본", "원본", "원본", author);

    @Test
    void 게시글_정상_생성() {
        assertDoesNotThrow(() ->
                new Article("title", "coverUrl", "contents", author));
    }

    @Test
    void 게시글_업데이트_성공() {
        Article editedArticle = new Article("수정됨", "수정됨", "수정됨", author);

        assertDoesNotThrow(() -> originalArticle.update(editedArticle));

        assertThat(originalArticle.getTitle()).isEqualTo(editedArticle.getTitle());
        assertThat(originalArticle.getCoverUrl()).isEqualTo(editedArticle.getCoverUrl());
        assertThat(originalArticle.getContents()).isEqualTo(editedArticle.getContents());
    }

    @Test
    void 게시글_업데이트_실패() {
        User notAuthor = new User("notAuthor", "not@mail.com", "Passw0rd!");
        Article editedArticle = new Article("수정됨", "수정됨", "수정됨", notAuthor);

        assertThrows(MismatchAuthorException.class, () -> originalArticle.update(editedArticle));
    }
}