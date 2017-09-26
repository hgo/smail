package me.hgo.smail;

public class MailConfig {
    private int port;
    private String host = "localhost";
    private String protocol;
    private String channel;
    private String user;
    private String pass;
    private String authenticatorClass;
    private boolean debug;
    private boolean mock;

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getProtocol() {
        return protocol;
    }

    void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getChannel() {
        return channel;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }

    public String getAuthenticatorClass() {
        return this.authenticatorClass;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isMock() {
        return this.mock;
    }

    MailConfig(MailConfigBuilder builder) {
        this.port = builder.port;
        this.host = builder.host;
        this.protocol = builder.protocol;
        this.channel = builder.channel;
        this.user = builder.user;
        this.pass = builder.pass;
        this.authenticatorClass = builder.authenticatorClass;
        this.debug = builder.debug;
        this.mock = builder.mock;
    }
}
