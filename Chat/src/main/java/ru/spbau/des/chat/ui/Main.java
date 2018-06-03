package ru.spbau.des.chat.ui;

import ru.spbau.des.chat.messenger.Chat;
import ru.spbau.des.chat.messenger.Messenger;
import ru.spbau.des.chat.transport.ClientTransport;
import ru.spbau.des.chat.transport.ServerTransport;
import ru.spbau.des.chat.transport.Transport;

import java.io.IOException;

/**
 * Creates a Messenger with server or client transport and launches command line
 * interface with it
 */
public class Main {
    private static final String BAD_INPUT_MESSAGE = "Usage: \n" +
            "<username> server <port> or \n" +
            "<username> client <address> <port>.";
    private static final String COMMAND_SERVER = "server";
    private static final String COMMAND_CLIENT = "client";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(BAD_INPUT_MESSAGE);
            return;
        }
        String username = args[0];
        String command = args[1];
        Transport transport = null;
        switch (command) {
            case COMMAND_SERVER: {
                int port = Integer.valueOf(args[2]);
                try {
                    transport = new ServerTransport(port);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case COMMAND_CLIENT: {
                String host = args[2];
                int port = Integer.valueOf(args[3]);
                transport = new ClientTransport(host, port);
                break;
            }
            default: {
                System.out.println(BAD_INPUT_MESSAGE);
                return;
            }
        }
        Chat chat = new Messenger(transport, username);
        CLI cli = new CLI(chat);
        cli.run();
    }
}