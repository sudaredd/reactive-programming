package app.reactive.reactiveprogramming.backpressure;

import io.micrometer.core.instrument.Counter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Metrics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

@Slf4j
@Component
public class Producer {

  private Counter counter = Metrics.counter("producer");

  public Flux<Message<Long>> produce(int targetRate, long upto) {
    var delayBetweenEmits = 1000L / targetRate;

    Callable initialVal = () -> 1L;
    BiFunction<Long, SynchronousSink<Message<Long>>, Long> generator =
        (Long state, SynchronousSink<Message<Long>> sink) -> {
          sleep(delayBetweenEmits);
          if (state > upto) {
            sink.complete();
          }
          log.info("Emitted {}", state);
          sink.next(new Message(state));
          return state + 1;
        };

    return (Flux<Message<Long>>) Flux.generate(initialVal, generator);
  }

  @SneakyThrows
  private void sleep(long mills) {
    Thread.sleep(mills);
  }
}
