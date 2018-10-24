package se.kits.rsocket;

import io.rsocket.AbstractRSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.publisher.Mono;


public class RSocketSeverApp {
    public static void main(String[] args) throws InterruptedException {
        RSocketFactory.receive()
                .acceptor((setupPayload, reactiveSocket) -> Mono.just(new RSocketApi()))
                .transport(TcpServerTransport.create("localhost", 7000))
                .start()
                .subscribe();

        Thread.currentThread().join();
    }

    private static class RSocketApi extends AbstractRSocket {


    }
}
