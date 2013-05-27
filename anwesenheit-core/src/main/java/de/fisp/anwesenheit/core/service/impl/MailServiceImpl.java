package de.fisp.anwesenheit.core.service.impl;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fisp.anwesenheit.core.service.MailService;

public class MailServiceImpl implements MailService {
  private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
  private Session session;

  public MailServiceImpl(Session session) {
    this.session = session;
  }

  private Session getSession() {
    return this.session;
  }

  @Override
  public void sendeMail(String subject, String text, String from, String adressen) {
    MimeMessage msg = new MimeMessage(getSession());
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
    } catch (MessagingException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Fehler beim Mailversand", e);
    }

  }
}
