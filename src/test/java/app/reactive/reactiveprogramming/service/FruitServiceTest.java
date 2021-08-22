package app.reactive.reactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.tools.agent.ReactorDebugAgent;

class FruitServiceTest {

    FruitService fruitService = new FruitService();

    @Test
    void multiFruits() {
        var fruits = fruitService.multiFruits();
        StepVerifier.create(fruits)
            .expectNext("Apple", "Banana", "Cantaloupe", "Watermelon")
            .verifyComplete();
    }

    @Test
    void multiFruitsFilterMap() {
        var fruits = fruitService.multiFruitsFilterMap(6);
        StepVerifier.create(fruits)
            .expectNext("CANTALOUPE", "WATERMELON")
            .verifyComplete();
    }

    @Test
    void uniqueFruit() {
        var fruit = fruitService.uniqueFruit();
        StepVerifier.create(fruit)
            .expectNext("Apple")
            .verifyComplete();
    }

    @Test
    void multiFruitsFlatMap() {
        var fruits = fruitService.multiFruitsFlatMap();
        StepVerifier.create(fruits)
            .expectNextCount(31)
            .verifyComplete();
    }

    @Test
    void multiFruitsFlatMapAsync() {
        var fruits = fruitService.multiFruitsFlatMapAsync();
        StepVerifier.create(fruits)
            .expectNextCount(17)
            .verifyComplete();
    }

    @Test
    void multiFruitsConcatMapAsync() {
        var fruits = fruitService.multiFruitsConcatMapAsync();
        StepVerifier.create(fruits)
            .expectNextCount(17)
            .verifyComplete();
    }

    @Test
    void fruitMonoFlatmapMany() {
        var fruits = fruitService.fruitMonoFlatmapMany();
        StepVerifier.create(fruits)
            .expectNextCount(5)
            .verifyComplete();
    }

    @Test
    void fruitsFilterTransform() {
        var fruits = fruitService.fruitsFluxFilterTransform(6);
        StepVerifier.create(fruits)
            .expectNext("Cantaloupe", "Watermelon")
            .verifyComplete();
    }

    @Test
    void fruitsFluxTransformDefaultIfEmpty() {
        var fruits = fruitService.fruitsFluxTransformDefaultIfEmpty(10);
        StepVerifier.create(fruits)
            .expectNext("Default")
            .verifyComplete();
    }

    @Test
    void fruitsFluxTransformSwitchIfEmpty() {
        var fruits = fruitService.fruitsFluxTransformSwitchIfEmpty(10);
        StepVerifier.create(fruits)
            .expectNext("Pineapple", "Banana")
            .verifyComplete();

    }

    @Test
    void fruitsFLuxConcat() {
        var fruitsAndVeggies = fruitService.fruitsFLuxConcat();
        StepVerifier.create(fruitsAndVeggies)
            .expectNext("Apple", "Banana", "Mango", "Egg plant", "Okra")
            .verifyComplete();
    }

    @Test
    void fruitsFLuxConcatWith() {
        var fruitsAndVeggies = fruitService.fruitsFLuxConcatWith();
        StepVerifier.create(fruitsAndVeggies)
            .expectNext("Apple", "Banana", "Mango", "Egg plant", "Okra")
            .verifyComplete();
    }

    @Test
    void fruitsMonoConcatWith() {
        var fruitsAndVeggies = fruitService.fruitsMonoConcatWith();
        StepVerifier.create(fruitsAndVeggies)
            .expectNext("Apple", "Okra")
            .verifyComplete();
    }

    @Test
    void fruitsFLuxMerge() {
        var fruitsAndVeggies = fruitService.fruitsFluxMerge();
        StepVerifier.create(fruitsAndVeggies)
            .expectNext("Apple", "Egg plant", "Banana", "Okra", "Mango")
            .verifyComplete();
    }

    @Test
    void fruitsFLuxMergeWith() {
        var fruitsAndVeggies = fruitService.fruitsFluxMergeWith();
        StepVerifier.create(fruitsAndVeggies)
            .expectNext("Apple", "Egg plant", "Banana", "Okra", "Mango")
            .verifyComplete();
    }

    @Test
    void fruitsFluxMergeSequential() {
        var fruitsAndVeggies = fruitService.fruitsFluxMergeSequential();
        StepVerifier.create(fruitsAndVeggies)
            .expectNext("Apple", "Banana", "Mango", "Egg plant", "Okra")
            .verifyComplete();
    }

    @Test
    void fruitsFluxZip() {
        var fruitWithVeggies = fruitService.fruitsFluxZip();
        StepVerifier.create(fruitWithVeggies)
            .expectNext("Apple:Egg plant", "Banana:Okra")
            .verifyComplete();
    }

    @Test
    void fruitsFluxZipWith() {
        var fruitWithVeggies = fruitService.fruitsFluxZipWith();
        StepVerifier.create(fruitWithVeggies)
            .expectNext("Apple:Egg plant", "Banana:Okra")
            .verifyComplete();
    }

    @Test
    void multiFruitsFilterDoOnNext() {
        var fruits = fruitService.multiFruitsFilterDoOnNext(6);
        StepVerifier.create(fruits)
            .expectNext("Cantaloupe", "Watermelon")
            .verifyComplete();
    }

    @Test
    void fruitFluxOnErrorReturn() {
        var fruits = fruitService.fruitFluxOnErrorReturn(4);
        StepVerifier.create(fruits)
            .expectNext("Orange")
            .verifyComplete();
    }

    @Test
    void fruitFluxOnErrorContinue() {
        var fruits = fruitService.fruitFluxOnErrorContinue();
        StepVerifier.create(fruits)
            .expectNext("Apple", "Banana")
            .verifyComplete();
    }

    @Test
    void fruitFluxOnErrorMap() {
        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
        var fruits = fruitService.fruitFluxOnErrorMap();
        StepVerifier.create(fruits)
            .expectNext("Apple")
            .expectError(IllegalStateException.class)
            .verify();
    }

    @Test
    void fruitFluxDoOnError() {
        var fruits = fruitService.fruitFluxDoOnError();
        StepVerifier.create(fruits)
            .expectNext("Apple")
            .expectError(RuntimeException.class)
            .verify();
    }
}