package app.reactive.reactiveprogramming.service;

import app.reactive.reactiveprogramming.domain.Book;
import app.reactive.reactiveprogramming.domain.BookInfo;
import app.reactive.reactiveprogramming.domain.Review;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
public class BookService {

    private BookInfoService bookInfoService;
    private ReviewService reviewService;

    public BookService(BookInfoService bookInfoService, ReviewService reviewService) {
        this.bookInfoService = bookInfoService;
        this.reviewService = reviewService;
    }

    public Flux<Book> books() {
        Flux<BookInfo> books = bookInfoService.getBooks();

        return books
            .flatMap(bookInfo -> {
                    Mono<List<Review>> reviews = reviewService.reviews(bookInfo.getBookId()).collectList();
                    return reviews
                        .map(re -> new Book(bookInfo, re)).log();
                }
            );
    }

    public Mono<Book> findBookById(long bookId) {
        Mono<BookInfo> bookInfo = bookInfoService.getBookById(bookId).subscribeOn(Schedulers.parallel());
        Mono<List<Review>> reviews = reviewService.reviews(bookId).collectList().subscribeOn(Schedulers.parallel());
       return bookInfo.zipWith(reviews)
            .map(tup -> new Book(tup.getT1(), tup.getT2()))
           .log();
     /*   return Mono.zip(bookInfo, reviews)
            .map(tup -> new Book(tup.getT1(), tup.getT2()));*/
    }
}
