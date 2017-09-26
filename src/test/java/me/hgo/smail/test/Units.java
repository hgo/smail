package me.hgo.smail.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.hgo.smail.*;

public class Units {

    static final Logger logger = LoggerFactory.getLogger(Units.class);
    final String host = "localhost";
    final String protocol = "smtp";
    final String channel = "starttls";
    final String pass = "ok";
    final String user = "user";
    final String EMAIL_ADDRESS = "address@hgo.me";
    MailConfig config = null;

    @Before
    public void before() {
        config = new MailConfigBuilder()
                .port(25)
                .host(host)
                .protocol(protocol)// smtp,smtps
                .channel(channel)// clear,starttls,ssl
                .pass(pass)
                .user(user)
                .debug(true)
                .build();
    }

    @Test
    public void test_config_builder() {
        assertEquals(25, config.getPort());
        assertEquals(host, config.getHost());
        assertEquals(protocol, config.getProtocol());
        assertEquals(channel, config.getChannel());
        assertEquals(pass, config.getPass());
        assertEquals(user, config.getUser());
    }

    @Test
    public void session_create() {
        Session session = SessionCreator.createWithConfig(config);
        assertNotNull(session);
    }

    @Test(expected = IllegalArgumentException.class)
    public void email_build_illegal() {
        new EmailBuilder().build();
    }

    @Test
    public void email_build_success() {
        Sendable email = new EmailBuilder()
                .content("Hi guys?")
                .subject("Subjecte")
                .contentType(EmailConstants.TEXT_PLAIN)
                .from("data@email.com")
                .recipients("rece@email.com")
                .build();
        assertNotNull(email);
    }

    @Test
    public void email_build_and_send() throws EmailException, IOException, MessagingException {
        Sendable email = new EmailBuilder()
                .content("Hi guys!")
                .subject("Subject")
                .contentType(EmailConstants.TEXT_PLAIN)
                .from(EMAIL_ADDRESS)
                .recipients(EMAIL_ADDRESS)
                .build();
        MailConfig gmailConfig = new MailConfigBuilder()
                .host("smtp.gmail.com")
                .user(EMAIL_ADDRESS)
                .pass("xxx")
                .channel("starttls")
                .debug(true)
                .protocol("smtp")
                .port(587)
                .mock()
                .build();
        email.send(gmailConfig);
        final Email latestEmail = TestMailContext.getLatestEmail(EMAIL_ADDRESS);
        assertNotNull(latestEmail);
        assertEquals("Subject", latestEmail.getSubject());
        final String parsedContent = TestMailContext.getParsedContent(latestEmail);
        logger.info(parsedContent);
        assertEquals("Hi guys?", parsedContent);
    }
    
    //TODO: html
    //TODO: attachments 

}
