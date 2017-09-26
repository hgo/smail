package me.hgo.smail;

import java.util.concurrent.Future;

import org.apache.commons.mail.EmailException;

public interface Sendable {

    public void send(MailConfig config) throws EmailException;

    public Future<String> sendAsync(MailConfig config) throws EmailException;

}
