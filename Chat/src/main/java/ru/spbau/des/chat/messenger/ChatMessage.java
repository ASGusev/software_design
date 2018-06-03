package ru.spbau.des.chat.messenger;

import ru.spbau.des.chat.transport.TransportMessage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * ChatMessage can be sent through a Chat
 */
public class ChatMessage {
    private final String text;
    private final String author;
    private final LocalDateTime time;

    private ChatMessage(String text, String author, LocalDateTime time) {
        this.text = text;
        this.author = author;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    protected TransportMessage toProtocolMessage() {
        return TransportMessage.getBuilder()
                .setText(text)
                .setAuthor(author)
                .setTime(time)
                .build();
    }

    public static class Builder {
        private String text;
        private String author;
        private LocalDateTime time;

        private Builder() {}

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setTime(LocalDateTime time) {
            this.time = time.truncatedTo(ChronoUnit.SECONDS);
            return this;
        }

        protected Builder loadTransportMessage(TransportMessage transportMessage) {
            text = transportMessage.getText();
            author = transportMessage.getAuthor();
            time = transportMessage.getTime();
            return this;
        }

        public ChatMessage build() {
            return new ChatMessage(text, author, time);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ChatMessage && ((ChatMessage) o).author.equals(author) &&
                ((ChatMessage) o).text.equals(text) && ((ChatMessage) o).time.equals(time);
    }
}
