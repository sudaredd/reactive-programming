package app.reactive.reactiveprogramming;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class HotAndColdStreamTest {

    @Test
    public void coldStreamTest() {
        var numbers = Flux.range(1, 10);

        numbers.subscribeOn(Schedulers.parallel()).subscribe(integer -> System.out.println("subscriber1 = " + integer));
        numbers.subscribeOn(Schedulers.parallel()).subscribe(integer -> System.out.println("subscriber2 = " + integer));
    }

    @Test
    @SneakyThrows
    public void hotStreamTest() {
        var numbers = Flux.range(1, 10)
                            .delayElements(Duration.ofMillis(1000));

        ConnectableFlux<Integer> publish = numbers.publish();
        publish.connect();

        publish.subscribe(integer -> System.out.println("subscriber1 = " + integer));
        Thread.sleep(3000);
        publish.subscribe(integer -> System.out.println("subscriber2 = " + integer));
        Thread.sleep(11000);

    }
}
