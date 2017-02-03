package com.zc;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class Email {
    //Sends email containing SQL query results
    public static void eSend(double total, List<String> item) {

        //Sending email account username and password
        final String username = "info@zcollectionsf.com";
        final String password = "94133";

        //Email account server settings
        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        //Composes the email and sends it using above account information
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("info@zcollectionsf.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("joshc@zcollectionsf.com"));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(""));
            message.setSubject("Sale Made at Hermitage");
            message.setText("Hermitage has made a sale in the amount of $" + total + ". Containing: \n" + item);

            Transport.send(message);

            System.out.println("Done - Sale");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public static void eSendOrder(double total, double deposit, List<String> item) {

        //Sending email account username and password
        final String username = "info@zcollectionsf.com";
        final String password = "94133";

        //Email account server settings
        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        //Composes the email and sends it using above account information
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("info@zcollectionsf.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("joshc@zcollectionsf.com"));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(""));
            message.setSubject("Layaway Made at Hermitage");
            message.setText("Hermitage has made a layaway in the amount of $" + (total) + " with a deposit of $" + deposit + ". Containing: \n" + item);

            Transport.send(message);

            System.out.println("Done - Layaway");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public static void eSendPayment(double payment) {

        //Sending email account username and password
        final String username = "info@zcollectionsf.com";
        final String password = "94133";

        //Email account server settings
        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        //Composes the email and sends it using above account information
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("info@zcollectionsf.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("joshc@zcollectionsf.com"));
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(""));
            message.setSubject("Layaway Payment Made at Hermitage");
            message.setText("Hermitage has made a layaway payment in the amount of $" + payment);

            Transport.send(message);

            System.out.println("Done - Layaway Payment");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
