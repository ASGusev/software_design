package ru.spbau.des.chat.transport;

/**
 * TransportException is thrown in case of any errors in a transport
 */
public class TransportException extends Exception {
    TransportException(String message) {
        super(message);
    }
}
