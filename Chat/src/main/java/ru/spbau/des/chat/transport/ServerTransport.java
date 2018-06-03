package ru.spbau.des.chat.transport;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Transport for chat server. Maintains its own GRPC service
 */
public class ServerTransport extends BaseTransport {
    private final static String LOGGER_NAME = "serverLogger";

    private StreamObserver<Protocol.Message> outputObserver;
    private final Logger logger = LogManager.getLogger(LOGGER_NAME);

    public ServerTransport(int port) throws IOException {
        ServerBuilder.forPort(port)
                .addService(new ChatService())
                .build()
                .start();
        logger.info("Server started");
    }

    @Override
    public void send(TransportMessage message) throws TransportException {
        if (outputObserver == null) {
            logger.error("No connection");
            throw new TransportException("Server: no connection");
        }
        outputObserver.onNext(message.toProtocolMessage());
        logger.debug("Message sent");
    }

    @Override
    public void close() {
        outputObserver.onCompleted();
        logger.info("Server closed");
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
                    logger.debug("Message received");
                    pushToListener(transportMessage);
                }

                @Override
                public void onError(Throwable t) {
                    logger.error(t);
                    outputObserver = null;
                    TransportListener listener = getListener();
                    if (listener != null) {
                        listener.onDisconnected();
                    }
                }

                @Override
                public void onCompleted() {
                    logger.info("Connection closed");
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