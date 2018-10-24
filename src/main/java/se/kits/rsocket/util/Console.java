package se.kits.rsocket.util;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Scanner;

public abstract class Console {
    public static Flux<String> readLineIndefinitely() {
        return Flux.defer(() -> Flux.<String>create(
                sink -> {
                    var sc = new Scanner(System.in).useDelimiter("\\s*");
                    sink.next(sc.nextLine());
                    sink.complete();
                })
                .subscribeOn(Schedulers.newSingle("system.in")))
                .repeat();
    }
}
