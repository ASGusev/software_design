package ru.spbau.des.roguelike.operation;

public interface Dialog {
    String getMessage();

    void accept();

    void deny();
}
