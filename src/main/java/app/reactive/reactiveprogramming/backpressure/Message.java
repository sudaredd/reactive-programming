package app.reactive.reactiveprogramming.backpressure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Message<T> {
    public Message(T payload) {
        this.payload = payload;
        headers.put("TIME", System.nanoTime());
    }

    T payload;
    Map<String, Object> headers = new HashMap<>();
}
