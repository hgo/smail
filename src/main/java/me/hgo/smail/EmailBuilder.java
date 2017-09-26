package me.hgo.smail;

import static org.apache.commons.mail.EmailConstants.*;

import java.util.Arrays;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailBuilder {

    Logger logger = LoggerFactory.getLogger(EmailBuilder.class);

    String charset = UTF_8;
    Map<String, String> headers;
    String subject;
    String content;
    String contentType;
    InternetAddress[] recipients;
    EmailAttachment[] attachments;
    InternetAddress from;
    InternetAddress replyto;
    InternetAddress[] ccs;
    InternetAddress[] bccs;

    private final static String errorMessage = "Error occured while setting internet address for string ";

    private static InternetAddress[] StringToInternetAddress(String... strings) {
        if (strings == null || strings.length == 0) {
            throw new IllegalArgumentException("Empty array");
        }
        InternetAddress[] arr = new InternetAddress[strings.length];
        for (int i = 0; i < arr.length; i++) {
            try {
                arr[i] = new InternetAddress(strings[i]);
            } catch (AddressException e) {
                throw new IllegalArgumentException(errorMessage + strings[i]);
            }
        }
        return arr;
    }

    public EmailBuilder charset(String charset) {
        this.charset = charset;
        return this;
    }

    public EmailBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public EmailBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailBuilder content(String content) {
        this.content = content;
        return this;
    }

    public EmailBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public EmailBuilder recipients(InternetAddress... recipients) {
        this.recipients = recipients;
        return this;
    }

    public EmailBuilder recipients(String... recipients) {
        return recipients(StringToInternetAddress(recipients));
    }

    public EmailBuilder attachments(EmailAttachment... attachments) {
        this.attachments = attachments;
        return this;
    }

    public EmailBuilder from(InternetAddress from) {
        this.from = from;
        return this;
    }

    public EmailBuilder from(String from) {
        return from(StringToInternetAddress(from)[0]);
    }

    public EmailBuilder replyto(String replyto) {
        return replyto(StringToInternetAddress(replyto)[0]);
    }

    public EmailBuilder replyto(InternetAddress replyto) {
        this.replyto = replyto;
        return this;
    }

    public EmailBuilder ccs(InternetAddress... ccs) {
        this.ccs = ccs;
        return this;
    }

    public EmailBuilder ccs(String... ccs) {
        return ccs(StringToInternetAddress(ccs));
    }

    public EmailBuilder bccs(String... bccs) {
        return bccs(StringToInternetAddress(bccs));
    }

    public EmailBuilder bccs(InternetAddress... bccs) {
        this.bccs = bccs;
        return this;
    }

    public Sendable build() {
        check(this);
        Email email = getEmail();
        return new EmailWrapper(email);
    }

    private void check(EmailBuilder mailer) {
        if (SmailUtils.STRING.isBlank(charset)) {
            charset = UTF_8;
        }
        if (SmailUtils.STRING.isBlank(contentType)) {
            throw new IllegalArgumentException("contentType should be set");
        }
        if (from == null) {
            throw new IllegalArgumentException("from should be set");
        }
        if (recipients == null || recipients.length == 0) {
            throw new IllegalArgumentException("recipients should be set");
        }
        if (SmailUtils.STRING.isBlank(subject)) {
            throw new IllegalArgumentException("subject should be set");
        }
    }

    public Email getEmail() {
        Email email = null;
        try {
            if (isPlain() && noAttachments()) {
                email = new SimpleEmail();
                email.setMsg(content);
            } else {
                HtmlEmail htmlEmail = new HtmlEmail();
                if (isPlain()) {
                    htmlEmail.setTextMsg(content);
                } else {
                    htmlEmail.setHtmlMsg(content);
                }
                email = htmlEmail;
            }
            email.setCharset(charset);

            if (!noAttachments()) {
                MultiPartEmail multiPartEmail = (MultiPartEmail) email;
                for (EmailAttachment attachment : attachments) {
                    multiPartEmail.attach(attachment);
                }
            }
            email.setFrom(from.getAddress(), from.getPersonal());
            email.setTo(Arrays.asList(recipients));
            email.setSubject(subject);

            if (replyto != null) {
                email.addReplyTo(replyto.getAddress(), replyto.getPersonal());
            }
            if (bccs != null) {
                email.setBcc(Arrays.asList(bccs));
            }
            if (ccs != null) {
                email.setBcc(Arrays.asList(ccs));
            }
            if (this.headers != null) {
                email.setHeaders(headers);
            }
            email.updateContentType(contentType);
        } catch (EmailException e) {
            logger.error("email creation exception");
            throw new RuntimeException(e);
        }
        return email;
    }

    private boolean noAttachments() {
        return attachments == null;
    }

    private boolean isPlain() {
        return TEXT_PLAIN.equals(contentType);
    }
}
