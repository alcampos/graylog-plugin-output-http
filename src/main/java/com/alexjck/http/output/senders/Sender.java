package com.alexjck.http.output.senders;

import org.graylog2.plugin.Message;

public interface Sender {

    void initialize();
    void stop();

    void send(Message message);

    boolean isInitialized();

}
