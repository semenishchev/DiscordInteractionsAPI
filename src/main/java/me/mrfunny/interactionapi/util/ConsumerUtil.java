package me.mrfunny.interactionapi.util;

import java.util.function.Consumer;

public class ConsumerUtil {
    public static <T> void accept(Consumer<T> consumer, T value) {
        if(consumer != null) {
            consumer.accept(value);
        }
    }
}
