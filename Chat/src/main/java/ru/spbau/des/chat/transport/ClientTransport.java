package ru.spbau.des.chat.transport;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Transport for chat clients. Relies on a GRPC service working on a server.
 */
public class ClientTransport extends BaseTransport {

    public static final String MSG_NO_CONNECTION = "Client: no connection";
    private ChatGrpc.ChatStub stub;
    private StreamObserver<Protocol.Message> outputObserver;

    public ClientTransport(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = ChatGrpc.newStub(channel);
        start();
    }

    @Override
    public void send(TransportMessage message) throws TransportException {
        if (outputObserver == null) {
            start();
        }
        if (outputObserver == null) {
            throw new TransportException(MSG_NO_CONNECTION);
        }
        Protocol.Message protocolMessage = message.toProtocolMessage();
        outputObserver.onNext(protocolMessage);
    }

    protected void start() {
        outputObserver = stub.exchangeMessages(new StreamObserver<Protocol.Message>() {
            private final static int CONNECTION_ATTEMPTS = 3;
            private int connectionErrors = 0;

            @Override
            public void onNext(Protocol.Message value) {
                TransportMessage transportMessage = TransportMessage.getBuilder()
                        .loadProtocolMessage(value)
                        .build();
                pushToListener(transportMessage);
            }

            @Override
            public void onError(Throwable t) {
                if (++connectionErrors < CONNECTION_ATTEMPTS) {
                    stub.exchangeMessages(this);
                } else {
                    TransportListener listener = getListener();
                    if (listener != null) {
                        listener.onError(t.getMessage());
                    }
                }
            }

            @Override
            public void onCompleted() {
                outputObserver = null;
                TransportListener listener = getListener();
                if (listener != null) {
                    listener.onDisconnected();
                }
            }
        });
        TransportListener listener = getListener();
        if (listener != null) {
            listener.onConnected();
        }
    }

    @Override
    public void close() throws IOException {
        outputObserver.onCompleted();
    }
}
