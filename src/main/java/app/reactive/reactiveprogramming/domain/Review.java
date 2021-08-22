package app.reactive.reactiveprogramming.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Review {
    private long reviewId;
    private long bookId;
    private double ratings;
    private String comments;
}
