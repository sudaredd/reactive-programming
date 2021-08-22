package app.reactive.reactiveprogramming;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackPressureTest {

    @Test
    public void testBackPressure() {
        Flux<Integer> numbers = Flux.range(1, 100).log();

        //numbers.subscribe(con-> System.out.println("con = " + con));

        numbers.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(3);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("value = " + value);
                if (value == 3) {
                    cancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("complete");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                System.out.println("On ERROR");
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("cancelled");
            }
        });


    }
}
