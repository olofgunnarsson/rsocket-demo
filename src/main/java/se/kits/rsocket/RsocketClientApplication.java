package se.kits.rsocket;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

import java.time.Duration;


public class RsocketClientApplication {
    public static void main(String[] args) throws InterruptedException {
        RSocket socket = RSocketFactory.connect()
                .transport(TcpClientTransport.create("localhost", 7000))
                .start()
                .block();

        socket.requestChannel(
                Flux.range(1, 10)
                        .delayElements(Duration.ofMillis(10000))
                        .map(integer -> DefaultPayload.create("give me:" + integer)))
                .subscribe(payload -> System.out.println(payload.getDataUtf8()));

        Thread.currentThread().join();
    }

    private static void f(RSocket socket, Integer i) {
        socket.requestResponse(DefaultPayload.create("hello kits " + i + "!"))
                .doOnSubscribe($ -> System.out.println("start " + i))
                .flux()
                .doOnNext(payload -> System.out.println(payload.getDataUtf8()))
                .doOnComplete(() -> System.out.println("end: " + i))
                .subscribe();
    }


}


