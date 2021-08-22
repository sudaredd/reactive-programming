package app.reactive.reactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FruitService {

    public Flux<String> multiFruits() {
        return Flux.fromIterable(List.of("Apple", "Banana", "Cantaloupe", "Watermelon")).log();
    }

    public Flux<String> multiFruitsFilterMap(int length) {
        return Flux.fromIterable(List.of("Apple", "Banana", "Cantaloupe", "Watermelon"))
            .filter(s -> s.length() > length)
            .map(String::toUpperCase)
            .log();
    }

    public Flux<String> multiFruitsFlatMap() {
        return Flux.fromIterable(List.of("Apple", "Banana", "Cantaloupe", "Watermelon"))
            .flatMap(s -> Flux.just(s.split("")))
            .log();
    }

    public Flux<String> multiFruitsFlatMapAsync() {
        return Flux.fromIterable(List.of("Mango", "Orange", "Banana"))
            .flatMap(s -> Flux.just(s.split(""))
                .delayElements(Duration.ofMillis(
                    new Random().nextInt(500)
                )))
            .log();
    }

    public Flux<String> multiFruitsConcatMapAsync() {
        return Flux.fromIterable(List.of("Mango", "Orange", "Banana"))
            .concatMap(s -> Flux.just(s.split(""))
                .delayElements(Duration.ofMillis(
                    new Random().nextInt(1000)
                )))
            .log();
    }

    public Mono<String> uniqueFruit() {
        return Mono.just("Apple");
    }

    public Flux<String> fruitMonoFlatmapMany() {
        return Mono.just("Apple")
            .flatMapMany(s -> Flux.just(s.split("")))
            .log();
    }

    public Flux<String> fruitsFluxFilterTransform(int length) {
        Function<Flux<String>, Flux<String>> filterData = data -> data.filter(s -> s.length() > length);
        return Flux.fromIterable(List.of("Cantaloupe", "Watermelon"))
            .transform(filterData)
            .log();
    }

    public Flux<String> fruitsFluxTransformDefaultIfEmpty(int length) {
        Function<Flux<String>, Flux<String>> filterData = data -> data.filter(s -> s.length() > length);
        return Flux.fromIterable(List.of("Cantaloupe", "Watermelon"))
            .transform(filterData)
            .defaultIfEmpty("Default")
            .log();
    }

    public Flux<String> fruitsFluxTransformSwitchIfEmpty(int length) {
        Function<Flux<String>, Flux<String>> filterData = data -> data.filter(s -> s.length() > length);
        return Flux.fromIterable(List.of("Cantaloupe", "Watermelon"))
            .transform(filterData)
            .switchIfEmpty(Flux.just("Pineapple", "Banana"))
            .log();
    }

    public Flux<String> fruitsFLuxConcat() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango");
        Flux<String> veggies = Flux.just("Egg plant", "Okra");
        return Flux.concat(fruits, veggies).log();
    }

    public Flux<String> fruitsFLuxConcatWith() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango");
        Flux<String> veggies = Flux.just("Egg plant", "Okra");
        return fruits.concatWith(veggies).log();
    }

    public Flux<String> fruitsMonoConcatWith() {
        Mono<String> fruits = Mono.just("Apple");
        Mono<String> veggies = Mono.just("Okra");
        return fruits.concatWith(veggies).log();
    }

    public Flux<String> fruitsFluxMerge() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango").delayElements(Duration.ofMillis(40));
        Flux<String> veggies = Flux.just("Egg plant", "Okra").delayElements(Duration.ofMillis(45));
        return Flux.merge(fruits, veggies).log();
    }

    public Flux<String> fruitsFluxMergeWith() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango").delayElements(Duration.ofMillis(40));
        Flux<String> veggies = Flux.just("Egg plant", "Okra").delayElements(Duration.ofMillis(45));
        return fruits.mergeWith(veggies).log();
    }

    public Flux<String> fruitsFluxMergeSequential() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango").delayElements(Duration.ofMillis(40));
        Flux<String> veggies = Flux.just("Egg plant", "Okra").delayElements(Duration.ofMillis(45));
        return Flux.mergeSequential(fruits, veggies).log();
    }

    public Flux<String> fruitsFluxZip() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango").delayElements(Duration.ofMillis(40));
        Flux<String> veggies = Flux.just("Egg plant", "Okra").delayElements(Duration.ofMillis(45));
        return Flux.zip(fruits, veggies).map(t -> t.getT1() + ":" + t.getT2()).log();
    }

    public Flux<String> fruitsFluxZipWith() {
        Flux<String> fruits = Flux.just("Apple", "Banana", "Mango").delayElements(Duration.ofMillis(40));
        Flux<String> veggies = Flux.just("Egg plant", "Okra").delayElements(Duration.ofMillis(45));
        return fruits.zipWith(veggies).map(t -> t.getT1() + ":" + t.getT2()).log();
    }

    public Flux<String> multiFruitsFilterDoOnNext(int length) {
        return Flux.fromIterable(List.of("Apple", "Banana", "Cantaloupe", "Watermelon"))
            .filter(s -> s.length() > length)
            .doOnNext(s -> System.out.println("s = " + s))
            .doOnSubscribe(subscription -> System.out.println("subscription = " + subscription))
            .doOnComplete(() -> System.out.println("Completed!!"))
            .log();
    }

    public Flux<String> fruitFluxOnErrorReturn(int length) {
        return Flux.just("Apple", "Banana")
            .map(m -> {
                if (m.length() > length) {
                    throw new RuntimeException("more than " + length);
                }
                return m;
            }).onErrorReturn("Orange").log();
    }

    public Flux<String> fruitFluxOnErrorContinue() {
        return Flux.just("Apple", "Orangle", "Banana")
            .map(m -> {
                if ("Orangle".equals(m)) {
                    throw new RuntimeException("Not a fruit than ");
                }
                return m;
            }).onErrorContinue((t, f) -> {
                    System.out.println("exception = " + t);
                    System.out.println("f = " + f);
                }
            ).log();
    }

    public Flux<String> fruitFluxOnErrorMap() {
        return Flux.just("Apple", "Orangle", "Banana")
            .map(m -> {
                if ("Orangle".equals(m)) {
                    throw new RuntimeException("Not a fruit");
                }
                return m;
            }).onErrorMap((t) -> {
                    System.out.println("exception = " + t);
                    throw new IllegalStateException("mapped error to ");
                }
            ).log();
    }
    public Flux<String> fruitFluxDoOnError() {
        return Flux.just("Apple", "Orangle", "Banana")
            .map(m -> {
                if ("Orangle".equals(m)) {
                    throw new RuntimeException("Not a fruit");
                }
                return m;
            }).doOnError((t) -> {
                    System.out.println("exception = " + t);
                }
            ).log();
    }
}
