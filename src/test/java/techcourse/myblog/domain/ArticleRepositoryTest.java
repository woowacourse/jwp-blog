package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleRepositoryTest {
    private static final ArticleDto SAMPLE_ARTICLE_DTO;
    private static final String TITLE = "TEST";
    private static final String COVER_URL = "http://article.com";
    private static final String CONTENTS = "This is Test";

    static {
        SAMPLE_ARTICLE_DTO = new ArticleDto(TITLE, COVER_URL, CONTENTS);
    }

    private ArticleRepository articleRepository;

    @BeforeEach
    public void setUp() {
        articleRepository = new ArticleRepository();
    }

    @Test
    @DisplayName("Article_추가_테스트")
    public void addTest() {
        long id = articleRepository.add(SAMPLE_ARTICLE_DTO);
        assertEquals(SAMPLE_ARTICLE_DTO, articleRepository.findById(id));
    }

    @Test
    @DisplayName("모든_Article_조회_테스트")
    public void findAllTest1() {
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        articleRepository.add(SAMPLE_ARTICLE_DTO);

        List<ArticleDto> actual = articleRepository.findAll();
        List<ArticleDto> expected = Arrays.asList(
                SAMPLE_ARTICLE_DTO,
                SAMPLE_ARTICLE_DTO,
                SAMPLE_ARTICLE_DTO);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ID로_Article_조회")
    public void findById1() {
        final long expectId;
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        expectId = articleRepository.add(SAMPLE_ARTICLE_DTO);

        ArticleDto expected = SAMPLE_ARTICLE_DTO;
        ArticleDto actual = articleRepository.findById(expectId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ID로_Article_갱신")
    public void updateById1() {
        long updateId;
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        updateId = articleRepository.add(SAMPLE_ARTICLE_DTO);

        ArticleDto expected = new ArticleDto("UPDATE", COVER_URL, "UPDATE CONTENTS");

        long actualId = articleRepository.updateById(expected, updateId);
        ArticleDto actual = articleRepository.findById(actualId);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ID로_Article_삭제")
    public void deleteById1() {
        int expected = 2;
        long deleteId;

        articleRepository.add(SAMPLE_ARTICLE_DTO);
        deleteId = articleRepository.add(SAMPLE_ARTICLE_DTO);
        articleRepository.add(SAMPLE_ARTICLE_DTO);

        articleRepository.deleteById(deleteId);
        assertEquals(expected, articleRepository.findAll().size());
    }

    @Test
    @DisplayName("Repository내의_모든_Article_삭제")
    public void deleteAll() {
        int expected = 0;
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        articleRepository.add(SAMPLE_ARTICLE_DTO);
        articleRepository.deleteAll();
        assertEquals(expected, articleRepository.findAll().size());
    }
}