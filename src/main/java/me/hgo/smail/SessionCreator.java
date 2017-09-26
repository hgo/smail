package me.hgo.smail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionCreator {

    static Logger logger = LoggerFactory.getLogger(SessionCreator.class);
    
    public static Session createWithConfig(MailConfig config){
        Session session = null;
        if (session == null) {
            Properties props = new Properties();
            props.put("mail.smtp.host", config.getHost());
            props.put("mail.smtp.port", config.getPort());

            if (config.getChannel().equals("clear")) {
                if(config.getPort() == 0){
                    props.put("mail.smtp.port", "25");
                }
            } else if (config.getChannel().equals("ssl")) {
                // port 465 + setup yes ssl socket factory (won't verify that the server certificate is signed with a root ca.)
                if(config.getPort() == 0){
                    props.put("mail.smtp.port", "465");
                    props.put("mail.smtp.socketFactory.port", "465");
                }
                props.put("mail.smtp.socketFactory.class", "me.hgo.mailqueue.YesSSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
            } else if (config.getChannel().equals("starttls")) {
                // port 25 + enable starttls + ssl socket factory
                if(config.getPort() == 0){
                    props.put("mail.smtp.port", "25");
                }
                props.put("mail.smtp.starttls.enable", "true");
            }

            String user = config.getUser();
            String password = config.getPass();
            String authenticator = config.getAuthenticatorClass();

            if (authenticator != null) {
                props.put("mail.smtp.auth", "true");
                try {
                    session = Session.getInstance(props, (Authenticator) SessionCreator.class.getClassLoader().loadClass(authenticator).newInstance());
                } catch (Exception e) {
                    logger.error("Cannot instanciate custom SMTP authenticator ({})", authenticator);
                    logger.error("Exception", e);
                }
            }

            if (session == null) {
                if (user != null && password != null) {
                    props.put("mail.smtp.auth", "true");
                    session = Session.getInstance(props, new SMTPAuthenticator(user, password));
                } else {
                    props.remove("mail.smtp.auth");
                    session = Session.getInstance(props);
                }
            }
            session.setDebug(config.isDebug());
        }
        return session;
    }
    
    public static class SMTPAuthenticator extends Authenticator {

        private String user;
        private String password;

        public SMTPAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password);
        }
    }
}
