package techcourse.myblog.service.dto;

import techcourse.myblog.domain.Comment;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CommentResponseDtoCollection implements Iterable<CommentResponseDto> {
    private final List<CommentResponseDto> commentResponseDtos;

    public CommentResponseDtoCollection(List<CommentResponseDto> commentResponseDtos) {
        this.commentResponseDtos = commentResponseDtos;
    }

    public static CommentResponseDtoCollection of(List<Comment> comments) {
        return new CommentResponseDtoCollection(comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()));
    }


    @Override
    public Iterator<CommentResponseDto> iterator() {
        return commentResponseDtos.iterator();
    }
}
