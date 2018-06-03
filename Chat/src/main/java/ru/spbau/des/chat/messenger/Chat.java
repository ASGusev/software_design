package ru.spbau.des.chat.messenger;

import java.io.Closeable;

/**
 * Chat allows to exchange messages. When a message is received, it is passed to all
 * subscribed callbacks
 */
public interface Chat extends Closeable {
    void sendMessage(String message) throws ChatException;

    void subscribe(ChatListener listener);

    void unsubscribe(ChatListener listener);
}
