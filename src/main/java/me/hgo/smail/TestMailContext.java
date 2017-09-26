package me.hgo.smail;

import static me.hgo.smail.SmailUtils.STRING.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.util.MimeMessageParser;

public class TestMailContext {

    static Queue<Email> queue = new ConcurrentLinkedQueue<Email>();

    static boolean push(Email email) {
        return queue.add(email);
    }

    public static Email getLatestEmail(String emailAddress) {
        if (SmailUtils.STRING.isBlank(emailAddress)) {
            return null;
        }
        ArrayList<Email> temp = new ArrayList<Email>(queue);
        Email found = null;
        for (int i = temp.size() - 1; i >= 0 && found == null; i--) {
            Email anEmail = temp.get(i);
            if (isSameEmailAddress(emailAddress, anEmail)) {
                found = anEmail;
            }
        }
        return found;
    }

    public static List<Email> getEmailsOf(String emailAddress) {
        if (isBlank(emailAddress)) {
            return Collections.emptyList();
        }
        ArrayList<Email> temp = new ArrayList<Email>(queue);
        ArrayList<Email> foundEmails = new ArrayList<Email>();
        for (int i = temp.size() - 1; i >= 0 && foundEmails.size() == 0; i--) {
            Email anEmail = temp.get(i);
            if (isSameEmailAddress(emailAddress, anEmail)) {
                foundEmails.add(anEmail);
            }
        }
        return foundEmails;
    }

    private static boolean isSameEmailAddress(String emailAddress, Email anEmail) {
        return anEmail.getFromAddress().getAddress().equals(emailAddress.toLowerCase());
    }

    public static String getParsedContent(Email email) {
        MimeMessageParser parser = new MimeMessageParser(email.getMimeMessage());
        try {
            parser = parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (parser.hasPlainContent()) {
            return parser.getPlainContent();
        } else {
            return parser.getHtmlContent();
        }
    }
}
