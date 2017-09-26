# smail
small, simple java mailing. mock context included

## Usage

```java
// create your global config
MailConfig gmailConfig = new MailConfigBuilder()
  .host("smtp.gmail.com")
  .user(EMAIL_ADDRESS)
  .pass("xxx")
  .channel("starttls")
  .debug(true)
  .protocol("smtp")
  .port(587)
  .mock() // MOCKED
  .build();

// create an email with fluent api
Sendable email = new EmailBuilder()
  .content("Hi guys!")
  .subject("Subject")
  .contentType(EmailConstants.TEXT_PLAIN)
  .from(EMAIL_ADDRESS)
  .recipients(EMAIL_ADDRESS)
  .build();

// and send it
email.send(gmailConfig);


// I build the config with mock()
// So I can get the email from TestMailContext
Email latestEmail = TestMailContext.getLatestEmail(EMAIL_ADDRESS);
assertNotNull(latestEmail);
assertEquals("Subject", latestEmail.getSubject());
String parsedContent = TestMailContext.getParsedContent(latestEmail);
logger.info(parsedContent);
assertEquals("Hi guys?", parsedContent);

```
