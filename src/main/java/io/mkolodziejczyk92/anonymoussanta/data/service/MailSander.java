package io.mkolodziejczyk92.anonymoussanta.data.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSander {

    String username = "anonymous.santa.reg@gmail.com";
    String password = "anonymoussantaadmin";

    public void sendEmailWithInvitation(
            String name,
            String surname,
            String eventName,
            String eventId,
            String email,
            String eventCode) {

        String text = String.format(
                "Hello %s %s," +
                        "you have been invited to %s's event. " +
                        "Please register on our website and join the event by entering: the event id: %s, your email and the event password: %s." +
                        "If you already have an account log in and join the event." +
                        "Anonymous Team",
                name, surname, eventId, eventName, eventCode);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Anonymous Santa - invitation");
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("An error occurred during the send an invitation: " + e.getMessage());
        }

    }

    public void sendEmailAfterDraw(
            String name,
            String surname,
            String eventName,
            String email) {

        String text = String.format(
                "Hello %s %s," +
                        "The event %s you have joined has just begun its next phase. " +
                        "We have drawn a person for whom you will be an anonymous Santa this year. " +
                        "Log in and see who you will buy a gift for!" +
                        "Anonymous Team",
                name, surname, eventName);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Anonymous Santa - draw");
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("An error occurred during the send an invitation: " + e.getMessage());
        }
    }
}
