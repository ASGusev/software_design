package ru.spbau.des.chat.transport;

/**
 * TransportListeners can subscribe on Transport's messages
 */
public interface TransportListener {
    /**
     * Is called when a message is received
     * @param message the received message
     */
    void onMessage(TransportMessage message);

    /**
     * Is called after a connection is set up
     */
    void onConnected();

    /**
     * Is called after disconnection
     */
    void onDisconnected();

    /**
     * Is called in case of connection attempt failure
     * @param message the message of the caught throwable
     */
    void onError(String message);
}
