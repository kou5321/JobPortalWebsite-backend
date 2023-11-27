package com.kou5321.jobPortalWebsite;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailTest {
    public static void main(String[] args) {
        final String username = "ethankou00@yahoo.com"; // replace with your email
        final String password = "k3iT8z9#p%BwSts"; // replace with your password or app-specific password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.yahoo.com"); // replace with your SMTP host
        props.put("mail.smtp.port", "587"); // or 465 (usually for SSL)
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // remove if using SSL

        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ethankou00@yahoo.com")); // replace with your email
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("kouyixiao@163.com")); // replace with recipient email
            message.setSubject("Test Email from JavaMail");
            message.setText("Hello,\n\n This is a test email!");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}