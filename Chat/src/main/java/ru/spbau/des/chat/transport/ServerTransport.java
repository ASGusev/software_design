package ru.spbau.des.chat.transport;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Transport for chat server. Maintains its own GRPC service
 */
public class ServerTransport extends BaseTransport {
    private StreamObserver<Protocol.Message> outputObserver;

    public ServerTransport(int port) throws IOException {
        ServerBuilder.forPort(port)
                .addService(new ChatService())
                .build()
                .start();
    }

    @Override
    public void send(TransportMessage message) throws TransportException {
        if (outputObserver == null) {
            throw new TransportException("Server: no connection");
        }
        outputObserver.onNext(message.toProtocolMessage());
    }

    @Override
    public void close() throws IOException {
        outputObserver.onCompleted();
    }

    private class ChatService extends ChatGrpc.ChatImplBase {
        @Override
        public StreamObserver<Protocol.Message> exchangeMessages(
                StreamObserver<Protocol.Message> responseObserver) {
            outputObserver = responseObserver;
            TransportListener listener = getListener();
            if (listener != null) {
                listener.onConnected();
            }
            return new StreamObserver<Protocol.Message>() {
                @Override
                public void onNext(Protocol.Message value) {
                    TransportMessage transportMessage = TransportMessage.getBuilder()
                            .loadProtocolMessage(value)
                            .build();
                    pushToListener(transportMessage);
                }

                @Override
                public void onError(Throwable t) {
                    outputObserver = null;
                    TransportListener listener = getListener();
                    if (listener != null) {
                        listener.onDisconnected();
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
            };
        }
    }
}