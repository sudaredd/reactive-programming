package app.reactive.reactiveprogramming.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class BookInfo {
    private long bookId;
    private String title;
    private String author;
    private String isbn;
}
