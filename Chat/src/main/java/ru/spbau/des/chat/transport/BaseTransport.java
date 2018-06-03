package ru.spbau.des.chat.transport;

/**
 * BaseTransport provides its subclasses with callback registration functionality.
 */
public abstract class BaseTransport implements Transport {
    private TransportListener listener;

    @Override
    public void subscribe(TransportListener listener) {
        this.listener = listener;
    }

    @Override
    public void unsubscribe() {
        this.listener = null;
    }

    protected TransportListener getListener() {
        return listener;
    }

    protected void pushToListener(TransportMessage message) {
        if (listener != null) {
            listener.onMessage(message);
        }
    }
}
