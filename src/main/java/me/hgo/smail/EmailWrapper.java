package me.hgo.smail;

import java.util.concurrent.*;

import javax.mail.Session;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

class EmailWrapper implements Sendable {

    private final Email email;
    private static final ExecutorService es = Executors.newSingleThreadExecutor();

    public EmailWrapper(Email email) {
        this.email = email;
    }

    public void send(MailConfig config) throws EmailException {
        setEmailSession(config);
        if (config.isMock()) {
            email.buildMimeMessage();
            TestMailContext.push(email);
        } else {
            email.send();
        }
    }

    public Future<String> sendAsync(MailConfig config) throws EmailException {
        setEmailSession(config);
        if (config.isMock()) {
            email.buildMimeMessage();
            TestMailContext.push(email);
            return es.submit(new Callable<String>() {
                public String call() throws Exception {
                    return TestMailContext.getParsedContent(email);
                }
            });
        } else {
            return es.submit(new Callable<String>() {
                public String call() throws Exception {
                    String result = email.send();
                    return result;
                }
            });

        }
    }

    private void setEmailSession(MailConfig config) {
        Session session = SessionCreator.createWithConfig(config);
        email.setMailSession(session);
    }

    @Override
    protected void finalize() throws Throwable {
        es.shutdown();
    }
}
