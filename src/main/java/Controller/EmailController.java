package Controller;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.net.Authenticator;
import java.util.Properties;


/**
 * The EmailController class provides functionality for sending emails using the SMTP protocol.
 * It uses the JavaMail API to send emails through a configured SMTP server.
 */
public class EmailController {

    /**
     * Sends an email to the specified recipient email address.
     *
     * @param recipientEmail The email address of the recipient.
     * @param subject        The subject of the email.
     * @param body           The body content of the email.
     */
    public static void sendEmail(String recipientEmail, String subject, String body) {
        final String SENDER_EMAIL = "1210510MailApi@gmail.com";
        final String SENDER_PASSWORD = "lpgrbbpoxbimpayz";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("Falha ao enviar o email: " + e.getMessage());
        }
    }
}