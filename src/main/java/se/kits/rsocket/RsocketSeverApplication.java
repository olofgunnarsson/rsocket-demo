package se.kits.rsocket;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class RsocketSeverApplication {
    public static void main(String[] args) throws InterruptedException {
        RSocketFactory.receive()
                .acceptor((setupPayload, reactiveSocket) -> Mono.just(new MyAbstractRSocket()))
                .transport(TcpServerTransport.create("localhost", 7000))
                .start()
                .subscribe();

        Thread.currentThread().join();
    }


    private static class MyAbstractRSocket extends AbstractRSocket {

        @Override
        public Mono<Payload> requestResponse(Payload p) {
            return Mono.just(DefaultPayload.create("echo: " + p.getDataUtf8()));
        }

        @Override
        public Flux<Payload> requestStream(Payload payload) {
            return Flux.range(0, 10)
                    .map(i -> DefaultPayload.create(payload.getDataUtf8() + "" + i));
        }

        @Override
        public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
            return Flux.from(payloads)
                    .zipWith(Flux.range(0, Integer.MAX_VALUE),
                            (payload, integer) -> DefaultPayload.create("echo: request(" + payload.getDataUtf8() + ")" +
                                    " response(" + integer + ")"));

        }
    }
}
