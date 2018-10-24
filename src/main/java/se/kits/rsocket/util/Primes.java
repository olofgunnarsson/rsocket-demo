package se.kits.rsocket.util;

import reactor.core.publisher.Flux;

public abstract class Primes {
    public static Flux<Long> factorize(long number) {
        return Flux
                .<Long>create(sink -> {
                    long n = number;

                    for (long i = 2; i <= n / i; i++) {
                        while (n % i == 0) {
                            sink.next(i);
                            n /= i;
                        }
                    }
                    if (n > 1) {
                        sink.next(n);
                    }
                    sink.complete();
                })
                .distinct()
                .doOnNext(l -> System.out.println("found factor " + l + " for " + number));
    }
}
