package com.zc;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class RMSNotify {
    public static void main(String[] args) throws SQLException {
        String sqlHost = "jdbc:sqlserver://JOSH-IT\\SQLEXPRESS;databaseName=SF Golden Adventures";
        String uName = "sa";
        String uPass = "Zcsf4119!";
        Connection con = DriverManager.getConnection(sqlHost, uName, uPass);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Filer fl = new Filer();
            int oldTransNumber = fl.read();
            int transNumber = maxTransNum(con);
            //Checks for new transaction number
            if(transNumber > oldTransNumber) {
                int newTransNumber = maxTransNum(con);
                Filer wr = new Filer();
                wr.write(transNumber);
                //Gets new transaction information from DB
                String query = "SELECT TransactionNumber, Total, SalesTax FROM [Transaction] WHERE TransactionNumber = " + transNumber;
                double total = 0.00;
                double tax = 0.00;
                ResultSet rs = viewTable(con, query);
                while (rs.next()) {
                    total = rs.getDouble("Total");
                    tax = rs.getDouble("SalesTax");
                    transNumber = rs.getInt("TransactionNumber");
                }
                if((total - tax) >= 2000.00) {
                    eSend(total, tax);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Returns highest transaction number as an int
    public static int maxTransNum(Connection con) {
        Statement stmt = null;
        int result = 0;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String query = "DECLARE @transNumber int SELECT @transNumber = MAX(TransactionNumber) FROM [Transaction] SELECT TransactionNumber FROM [Transaction] WHERE TransactionNumber = @transNumber";
            ResultSet rs = viewTable(con, query);
            while (rs.next()) {
                result = rs.getInt("TransactionNumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {

        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    //Takes query and connects to SQL DB to run query. Places result of query in ResultSet rs
    public static ResultSet viewTable(Connection con, String query) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return rs;
    }
    //Sends email containing SQL query results
    public static void eSend(double total, double tax) {

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
            message.setText("Hermitage has made a sale in the amount of $" + (total - tax) + ".");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}


