package ru.spbau.des.roguelike.operation;

/**
 * Dialog contains a message and allows to accept or deny it
 */
public interface Dialog {
    String getMessage();

    void accept();

    void deny();
}
