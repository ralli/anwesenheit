package de.fisp.anwesenheit.core.service.impl;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.service.MailService;

public class MailServiceImpl implements MailService {
  private static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
  private String host;

  public MailServiceImpl(String host) {
    this.host = host;
  }

  private Session getMailSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", host);
    Session session = Session.getDefaultInstance(props, null);
    session.setDebug(true);
    return session;
  }

  @Override
  public void sendeMail(String subject, String text, String from, String adressen) {
    Session session = getMailSession();
    MimeMessage msg = new MimeMessage(session);
    try {
      if (log.isDebugEnabled()) {
        log.debug("Sending Mail: \n" + "To: " + adressen + "\n" + "Subject: " + subject + "\n" + "Text: " + text + "\n" + "From: "
            + from);
      }

      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adressen));
      msg.setFrom(new InternetAddress(from));
      msg.setSubject(subject);

      MimeBodyPart messagePart = new MimeBodyPart();

      MimeMultipart multipart = new MimeMultipart();
      multipart.addBodyPart(messagePart); // adding message part

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<body style=\"font-family:Arial,sans-serif;\">\n");
      stringBuilder.append(text);
      stringBuilder.append("</body>\n");

      if (log.isDebugEnabled()) {
        log.debug("Message-Text: " + stringBuilder.toString());
      }

      // Setting the Email Encoding
      messagePart.setText(stringBuilder.toString(), "utf-8");
      messagePart.setHeader("Content-Type", "text/html; charset=\"utf-8\"");
      messagePart.setHeader("Content-Transfer-Encoding", "quoted-printable");

      msg.setContent(multipart);
      msg.setSentDate(new Date());

      Transport.send(msg);
    } catch (AddressException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Fehler beim Mailversand", e);
    } catch (MessagingException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Fehler beim Mailversand", e);
    }

  }

}
