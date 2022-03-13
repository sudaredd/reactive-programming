package app.reactive.reactiveprogramming.utils;

import lombok.SneakyThrows;

public class Util {

    @SneakyThrows
    public static void sleep(long mills) {
        Thread.sleep(mills);
    }
}
