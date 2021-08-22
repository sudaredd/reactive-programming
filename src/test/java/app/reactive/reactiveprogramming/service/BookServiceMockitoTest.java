package app.reactive.reactiveprogramming.service;

import app.reactive.reactiveprogramming.domain.Book;
import app.reactive.reactiveprogramming.exception.BookException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceMockitoTest {

    @Mock
    private BookInfoService bookInfoService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private  BookService bookService;

    @Test
    void books() {

        Mockito.when(bookInfoService.getBooks())
            .thenCallRealMethod();
        Mockito.when(reviewService.reviews(Mockito.anyLong()))
            .thenCallRealMethod();

        var books = bookService.books();

        StepVerifier.create(books)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void booksOnError() {

        Mockito.when(bookInfoService.getBooks())
            .thenCallRealMethod();
        Mockito.when(reviewService.reviews(Mockito.anyLong()))
            .thenThrow(new IllegalStateException("exception using mock test"));

        var books = bookService.books();

        StepVerifier.create(books)
            .expectError(BookException.class)
            .verify();
    }

    @Test
    void booksOnErrorRetry() {

        Mockito.when(bookInfoService.getBooks())
            .thenCallRealMethod();
        Mockito.when(reviewService.reviews(Mockito.anyLong()))
            .thenThrow(new IllegalStateException("exception using mock test"));

        var books = bookService.booksRetry();

        StepVerifier.create(books)
            .expectError(BookException.class)
            .verify();
    }

    @Test
    void booksRetryWhen() {

        Mockito.when(bookInfoService.getBooks())
            .thenCallRealMethod();
        Mockito.when(reviewService.reviews(Mockito.anyLong()))
            .thenThrow(new IllegalStateException("exception using mock test"));

        var books = bookService.booksRetryWhen();

        StepVerifier.create(books)
            .expectError(BookException.class)
            .verify();
    }
}