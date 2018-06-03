package ru.spbau.des.chat.messenger;

/**
 * ChatException is thrown if an error happens during Chat work
 */
public class ChatException extends Exception {
    ChatException (String message) {
        super(message);
    }
}
