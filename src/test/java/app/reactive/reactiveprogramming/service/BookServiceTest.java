package app.reactive.reactiveprogramming.service;

import app.reactive.reactiveprogramming.domain.Book;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    BookService bookService = new BookService(new BookInfoService(), new ReviewService());

    @Test
    void books() {
        Flux<Book> books = bookService.books();

        StepVerifier.create(books)
            .assertNext(book -> {
                assertEquals("Java concurrency in practice", book.getBookInfo().getTitle());
                assertEquals(2, book.getReviews().size());
            })
            .assertNext(book -> {
                assertEquals("Effective Java", book.getBookInfo().getTitle());
                assertEquals(2, book.getReviews().size());
            })
            .assertNext(book -> {
                assertEquals("Java 8 in action", book.getBookInfo().getTitle());
                assertEquals(2, book.getReviews().size());
            })
            .verifyComplete();
    }

    @Test
    void findBookById() {
        Mono<Book> book = bookService.findBookById(1);
        StepVerifier.create(book)
            .assertNext(b -> {
                assertEquals("Java concurrency in practice", b.getBookInfo().getTitle());
                assertEquals(2, b.getReviews().size());
            })
        .verifyComplete();
    }
}