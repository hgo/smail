package me.hgo.smail;

public class MailConfigBuilder {
    int port;
    String host;
    String protocol;
    String channel;
    String user;
    String pass;
    String authenticatorClass;
    boolean debug;
    boolean mock;

    public MailConfigBuilder port(int port) {
        this.port = port;
        return this;
    }

    public MailConfigBuilder host(String host) {
        this.host = host;
        return this;
    }

    public MailConfigBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * clear, starttls or ssl
     */
    public MailConfigBuilder channel(String channel) {
        this.channel = channel;
        return this;
    }

    public MailConfigBuilder user(String user) {
        this.user = user;
        return this;
    }

    public MailConfigBuilder pass(String pass) {
        this.pass = pass;
        return this;
    }

    public MailConfigBuilder authenticatorClass(String authenticatorClass) {
        this.authenticatorClass = authenticatorClass;
        return this;
    }

    public MailConfigBuilder debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public MailConfigBuilder mock() {
        this.mock = true;
        return this;
    }

    public MailConfig build() {
        return new MailConfig(this);
    }
}