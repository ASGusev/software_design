package ru.spbau.des.chat.transport;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Transport for chat clients. Relies on a GRPC service working on a server.
 */
public class ClientTransport extends BaseTransport {
    private final static String MSG_NO_CONNECTION = "Client: no connection";
    private final static String LOGGER_NAME = "clientLogger";

    private ChatGrpc.ChatStub stub;
    private StreamObserver<Protocol.Message> outputObserver;
    private final Logger logger = LogManager.getLogger(LOGGER_NAME);

    public ClientTransport(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = ChatGrpc.newStub(channel);
        start();
        logger.info("Client started");
    }

    @Override
    public void send(TransportMessage message) throws TransportException {
        if (outputObserver == null) {
            logger.info("Connection lost");
            start();
        }
        if (outputObserver == null) {
            logger.error("Server not found");
            throw new TransportException(MSG_NO_CONNECTION);
        }
        Protocol.Message protocolMessage = message.toProtocolMessage();
        outputObserver.onNext(protocolMessage);
        logger.debug("Message sent");
    }

    private void start() {
        outputObserver = stub.exchangeMessages(new StreamObserver<Protocol.Message>() {
            private final static int CONNECTION_ATTEMPTS = 3;
            private int connectionErrors = 0;

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
    public void close() {
        outputObserver.onCompleted();
        logger.info("Client closed");
    }
}
