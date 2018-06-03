package ru.spbau.des.chat.ui;

import ru.spbau.des.chat.messenger.Chat;
import ru.spbau.des.chat.messenger.ChatException;
import ru.spbau.des.chat.messenger.ChatListener;
import ru.spbau.des.chat.messenger.ChatMessage;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Provides a command line interface for a Chat
 */
public class CLI implements ChatListener {
    private final static String DISCONNECTION_MSG = "Disconnected";

    private Chat chat;

    public CLI(Chat chat) {
        this.chat = chat;
    }

    public void run() {
        chat.subscribe(this);
        Scanner inputScanner = new Scanner(System.in);
        while (true) {
            try {
                String message = inputScanner.nextLine();
                chat.sendMessage(message);
            } catch (NoSuchElementException e) {
                break;
            } catch (ChatException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            chat.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onMessage(ChatMessage message) {
        System.out.printf("%s\t%s\n%s\n", message.getAuthor(), message.getTime(),
                message.getText());
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {
        System.out.println(DISCONNECTION_MSG);
    }

    @Override
    public void onError(String message) {
        System.out.println(message);
    }
}
