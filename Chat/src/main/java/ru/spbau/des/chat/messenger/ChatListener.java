package ru.spbau.des.chat.messenger;

/**
 * ChatListener can subscribe on Chat messages
 */
public interface ChatListener {
    void onMessage(ChatMessage message);

    void onConnected();

    void onDisconnected();

    void onError(String message);
}
