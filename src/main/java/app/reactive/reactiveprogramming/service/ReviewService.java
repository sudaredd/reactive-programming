package app.reactive.reactiveprogramming.service;

import app.reactive.reactiveprogramming.domain.Review;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReviewService {

    public Flux<Review> reviews(long bookId) {
       return Flux.fromIterable(
           List.of(
            new Review(1, bookId, 9.3, "Good book"),
            new Review(2, bookId, 9.0, "Worth reading")
        )
       );
    }
}
