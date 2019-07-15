package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleRepositoryTest {
    private static final long ID = 1L;
    private static final String TITLE = "TEST";
    private static final String COVER_URL = "http://article.com";
    private static final String CONTENTS = "This is Test";

    private ArticleRepository articleRepository;
    private Article sampleArticle;

    @BeforeEach
    public void setUp() {
        articleRepository = new ArticleRepository();
        sampleArticle = new Article();
        sampleArticle.setId(ID);
        sampleArticle.setTitle(TITLE);
        sampleArticle.setCoverUrl(COVER_URL);
        sampleArticle.setContents(CONTENTS);
    }

    @Test
    @DisplayName("Article_추가_테스트")
    public void addTest() {
        articleRepository.add(sampleArticle);
        assertEquals(sampleArticle, articleRepository.findById(ID));
    }

    @Test
    @DisplayName("모든_Article_조회_테스트")
    public void findAllTest() {
        articleRepository.add(sampleArticle);
        articleRepository.add(sampleArticle);
        articleRepository.add(sampleArticle);

        List<Article> actual = articleRepository.findAll();
        List<Article> expected = Arrays.asList(
                sampleArticle,
                sampleArticle,
                sampleArticle);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ID로_Article_조회")
    public void findById() {
        final long expectId = 2L;
        articleRepository.add(sampleArticle);
        articleRepository.add(sampleArticle);

        Article expected = new Article(expectId, TITLE, COVER_URL, CONTENTS);
        Article actual = articleRepository.findById(expectId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ID로_Article_갱신")
    public void updateById() {
        long updateId;
        Article expected = new Article(2L, "UPDATE", "https://update.com", "UPDATECON");
        articleRepository.add(sampleArticle);
        articleRepository.add(sampleArticle);
        updateId = articleRepository.add(sampleArticle);
        expected.setId(updateId);

        long actualId = articleRepository.updateById(expected, updateId);
        assertEquals(expected, articleRepository.findById(actualId));
    }

    @Test
    @DisplayName("ID로_Article_삭제")
    public void deleteById() {
//        long deleteId;
//
//        articleRepository.add(sampleArticle);
//        deleteId = articleRepository.add(sampleArticle);
//        articleRepository.add(sampleArticle);
//
//        articleRepository.deleteById(deleteId);
//        long actual = articleRepository.findAll().size();
//
//        assertEquals(2, actual);
    }

    @Test
    @DisplayName("Repository내의_모든_Article_삭제")
    public void deleteAll() {
        int expected = 0;
        articleRepository.add(sampleArticle);
        articleRepository.add(sampleArticle);
        articleRepository.deleteAll();
        assertEquals(expected, articleRepository.findAll().size());
    }
}