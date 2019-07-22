package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleWriteDto;

public class ArticleAssembler {
    public static Article writeArticle(ArticleWriteDto articleWriteDto) {
        return new Article(articleWriteDto.getTitle(),articleWriteDto.getContents(),articleWriteDto.getCoverUrl());
    }
}
