package app.reactive.reactiveprogramming.service;

import app.reactive.reactiveprogramming.domain.BookInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookInfoService {

    public Flux<BookInfo> getBooks() {
        var bookInfos = List.of(
            new BookInfo(1, "Java concurrency in practice", "Brain Gotz", "3846463466"),
            new BookInfo(2, "Effective Java", "Joshua Blotch", "986788"),
            new BookInfo(3, "Java 8 in action", "Raoul-Gabriel Urma", "653363663")
        );

        return Flux.fromIterable(bookInfos);
    }

    public Mono<BookInfo> getBookById(long bookId) {
        Map<Long, BookInfo> map = List.of (
            new BookInfo(1, "Java concurrency in practice", "Brain Gotz", "3846463466"),
            new BookInfo(2, "Effective Java", "Joshua Blotch", "986788"),
            new BookInfo(3, "Java 8 in action", "Raoul-Gabriel Urma", "653363663")
        )
            .stream()
            .collect(Collectors.toMap(s -> s.getBookId(), s -> s));

        return Mono.just(map.get(bookId));
    }
}
