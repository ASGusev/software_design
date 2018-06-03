package ru.spbau.des.chat.messenger;

import ru.spbau.des.chat.transport.Transport;
import ru.spbau.des.chat.transport.TransportException;
import ru.spbau.des.chat.transport.TransportMessage;
import ru.spbau.des.chat.transport.TransportListener;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Messenger is a chat that exchanges messages through provided Transport
 */
public class Messenger implements Chat, TransportListener {
    private final static String CONNECTION_MSG = "Connected";

    private Transport transport;
    private String author;
    private List<ChatListener> listeners = new ArrayList<>();

    public Messenger(Transport transport, String author) {
        this.transport = transport;
        this.author = author;
        transport.subscribe(this);
        onConnected();
    }

    @Override
    public void sendMessage(String message) throws ChatException {
        try {
            transport.send(TransportMessage.getBuilder()
                    .setText(message)
                    .setAuthor(author)
                    .setTime(LocalDateTime.now())
                    .build());
        } catch (TransportException e) {
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public void subscribe(ChatListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(ChatListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onMessage(TransportMessage message) {
        ChatMessage chatMessage = ChatMessage.getBuilder()
                .loadTransportMessage(message)
                .build();
        listeners.forEach(listener -> listener.onMessage(chatMessage));
    }

    @Override
    public void onConnected() {
        try {
            sendMessage(CONNECTION_MSG);
        } catch (ChatException e) {
            listeners.forEach(listener -> listener.onError(e.getMessage()));
        }
    }

    @Override
    public void onDisconnected() {
        listeners.forEach(ChatListener::onDisconnected);
    }

    @Override
    public void onError(String message) {
        listeners.forEach(listener -> listener.onError(message));
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
