package app.reactive.reactiveprogramming.service;

import app.reactive.reactiveprogramming.domain.Book;
import app.reactive.reactiveprogramming.domain.BookInfo;
import app.reactive.reactiveprogramming.domain.Review;
import app.reactive.reactiveprogramming.exception.BookException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
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
            ).onErrorMap(th -> {
                log.error("error in bookservice", th);
                throw new BookException("exception occurred while fetching books => " + th);
            }).log();
    }

    public Flux<Book> booksRetry() {
        Flux<BookInfo> books = bookInfoService.getBooks();

        return books
            .flatMap(bookInfo -> {
                    Mono<List<Review>> reviews = reviewService.reviews(bookInfo.getBookId()).collectList();
                    return reviews
                        .map(re -> new Book(bookInfo, re)).log();
                }
            ).onErrorMap(th -> {
                log.error("error in bookservice", th.getMessage());
                throw new BookException("exception occurred while fetching books => " + th.getMessage());
            })
            .retry(3)
            .log();
    }

    public Flux<Book> booksRetryWhen() {

        Flux<BookInfo> books = bookInfoService.getBooks();

        Retry retry = Retry.backoff(3, Duration.ofMillis(1000))
            .filter(throwable -> throwable instanceof BookException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                Exceptions.propagate(retrySignal.failure()));

        return books
            .flatMap(bookInfo -> {
                    Mono<List<Review>> reviews = reviewService.reviews(bookInfo.getBookId()).collectList();
                    return reviews
                        .map(re -> new Book(bookInfo, re)).log();
                }
            ).onErrorMap(th -> {
                log.error("error in bookservice", th.getMessage());
                throw new BookException("exception occurred while fetching books => " + th.getMessage());
            })
            .retryWhen(retry)
            .log();
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
