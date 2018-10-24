package se.kits.rsocket;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;


@SuppressWarnings("ALL")
public class RSocketClientApp {
    public static void main(String[] args) throws Exception {
        var socket = RSocketFactory.connect()
                .transport(TcpClientTransport.create("localhost", 7000))
                .start()
                .retry();

        Thread.currentThread().join();
    }
}


