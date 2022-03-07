package app.reactive.reactiveprogramming;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
public class ReactorStreamsTest {

  @Test
  public void testOnErrorContinue() {
    Flux.range(1, 5)
        .doOnNext(i -> System.out.println("input=" + i))
        .map(i -> i == 2 ? i / 0 : i)
        .map(i -> i * 2)
        .onErrorContinue(
            (err, i) -> log.info("onErrorContinue={}", i))
        .reduce(Integer::sum)
        .doOnNext(i -> System.out.println("sum=" + i))
        .block();
  }

  @Test
  public void testOnErrorResume() {
    Flux.range(1, 5)
        .doOnNext(i -> System.out.println("input=" + i))
        .map(i -> i == 2 ? i / 0 : i)
        .map(i -> i * 2)
        .onErrorResume(
            (err) -> {
              log.info("onErrorResume={}");
              return Flux.just(100);
            })
        .reduce((i, j) -> i + j)
        .doOnNext(i -> System.out.println("sum=" + i))
        .block();
  }

  @Test
  public void testGenerate() {
    AtomicInteger atomicInteger = new AtomicInteger();
    Flux<Greeting> hello =
        Flux.<Greeting>generate(
                sink ->
                    sink.next(
                        new Greeting(
                            "hello @ "
                                + atomicInteger.incrementAndGet()
                                + " ::"
                                + LocalDateTime.now())))
            .take(40)
            .delayElements(Duration.ofMillis(500))
            .log();
    StepVerifier.create(hello)
        //        .expectNext(new Greeting("hello").getMessage())
        .expectNextCount(40)
        .verifyComplete();
  }

  @Getter
  @ToString
  @EqualsAndHashCode
  @AllArgsConstructor
  static class Greeting {
    String message;
  }

  @Test
  public void testFlux() {
    Flux.fromIterable(List.of("hello", "hi", "xyz"))
        .onErrorContinue(
            (ex, obj) -> {
              log.info("Error occurred, returning default");
              //            return Flux.just("abc", "kkk");
            })
        .subscribe(
            w -> {
              if (w.equals("hi")) {
                int k = 1 / 0;
              }
              log.info("" + w);
              sleep(1000);
            });
  }

  @Test
  public void testColdPublisher_1() {
    Mono.fromSupplier(() -> getDataToBePublished())
        .log()
        .subscribe(i -> log.info("subscriber-1::" + i));
  }

  @Test
  public void testColdPublisher_2() {
    Flux<String> netFlux = Flux.fromStream(() -> getMovie()).delayElements(Duration.ofSeconds(2));

    // you start watching the movie
    netFlux.subscribe(scene -> log.info("subscriber 1 is watching " + scene));

    // I join after sometime
    sleep(5000);
    netFlux.subscribe(scene -> log.info("subscriber 2 is watching " + scene));
    sleep(11000);
  }

  @Test
  public void testHotPublisher_1() {
    Flux<String> netFlux =
        Flux.fromStream(() -> getMovie()).delayElements(Duration.ofSeconds(2)).share();

    // you start watching the movie
    netFlux.subscribe(scene -> log.info("subscriber 1 is watching " + scene));

    // I join after sometime
    sleep(5000);
    netFlux.subscribe(scene -> log.info("subscriber 2 is watching " + scene));
    sleep(11000);
  }

  /** First subscriber reads all data before second one starts */
  @Test
  public void testHotPublisher_2() {
    Flux<String> netFlux =
        Flux.fromStream(() -> getMovie()).delayElements(Duration.ofSeconds(2)).share();

    // you start watching the movie
    netFlux.subscribe(scene -> log.info("subscriber 1 is watching " + scene));

    // Second subscriber joins after first subscriber completes
    sleep(11000);
    netFlux.subscribe(scene -> log.info("subscriber 2 is watching " + scene));
    sleep(22000);
  }

  /**
   * Second subscriber subscribes after first subscriber reads all data. Flux use cache method.
   * Cache caches the history and multicasts to multiple subscribers.
   *
   * <p>In the below example, if you notice the output, we are NOT making movie streaming request
   * second time. However, second subscriber is able to watch all the scenes from the beginning as
   * it has cached all the items for future subscribers.
   */
  @Test
  public void testHotPublisher_3() {
    Flux<String> netFlux =
        Flux.fromStream(() -> getMovie()).delayElements(Duration.ofSeconds(2)).share().cache();

    // you start watching the movie
    netFlux.subscribe(scene -> log.info("subscriber 1 is watching " + scene));

    // Second subscriber joins after first subscriber completes
    sleep(11000);
    netFlux.subscribe(scene -> log.info("subscriber 2 is watching " + scene));
    sleep(22000);
  }

  private int getDataToBePublished() {
    log.info("getDataToBePublished was called");
    return 1;
  }

  private Stream<String> getMovie() {
    log.info("getMovie was called");
    return Stream.of("scene1", "scene2", "scene3", "scene4");
  }

  @SneakyThrows
  public void sleep(int mills) {
    Thread.sleep(mills);
  }
}
