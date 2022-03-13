package app.reactive.reactiveprogramming.backpressure;

import app.reactive.reactiveprogramming.utils.Util;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static app.reactive.reactiveprogramming.utils.Util.sleep;

@Slf4j
@Component
public class Consumer {

  Producer producer = new Producer();
  private ExecutorService executorService = Executors.newFixedThreadPool(5);
  private Scheduler publishOnScheduler = Schedulers.newBoundedElastic(5, 1000, "publish");
  private Scheduler subscribeOnScheduler = Schedulers.newBoundedElastic(5, 1000, "subscribe");
  private Scheduler flatMapScheduler = Schedulers.newBoundedElastic(10, 100, "flatmap");

  void scenario1() {
    int count = 100;
    var producerRate = 5;
    var consumerRate = 1;
    long delayBetweenConsumes = 1000L / consumerRate;
    executorService.submit(
        () -> {
          producer
              .produce(producerRate, count)
              .subscribe(
                  (message -> {
                    sleep(delayBetweenConsumes);
                    log.info("consumed {}", message.payload);
                  }));
        });

    sleep(100000);
  }

  void scenario2() {
    int count = 300;
    var producerRate = 10;
    var consumerRate = 5;
    long delayBetweenConsumes = 1000L / consumerRate;
    producer
        .produce(producerRate, count)
        .subscribeOn(subscribeOnScheduler)
        .publishOn(publishOnScheduler, 10)
        .subscribe(
            (message -> {
              sleep(delayBetweenConsumes);
              log.info("consumed {}", message.payload);
            }));

    sleep(400000);
  }

  void scenario3() {
    int count = 300;
    var producerRate = 100;
    var consumerRate = 3;
    long delayBetweenConsumes = 1000L / consumerRate;
    producer
        .produce(producerRate, count)
        .subscribeOn(subscribeOnScheduler)
        .publishOn(publishOnScheduler)
        .flatMap(
            val ->
                Mono.fromSupplier(
                        () -> {
                          sleep(delayBetweenConsumes);
                          log.info("consumed {}", val.payload);
                          return null;
                        })
                    .subscribeOn(flatMapScheduler))
        .subscribe();

    sleep(100000);
  }
}
