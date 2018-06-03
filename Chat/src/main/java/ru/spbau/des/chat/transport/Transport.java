package ru.spbau.des.chat.transport;

import java.io.Closeable;

/**
 * Transport is capable of sending and receiving messages. When a message is received,
 * it calls the registered callback.
 */
public interface Transport extends Closeable {
    void send(TransportMessage transportMessage) throws TransportException;

    void subscribe(TransportListener listener);

    void unsubscribe();
}
