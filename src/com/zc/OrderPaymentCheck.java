package com.zc;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.zc.Email.eSendPayment;

public class OrderPaymentCheck {

    private static String[][] parse;
    private static List<Double> newValues = new ArrayList<>();
    private static List<Double> oldValues = new ArrayList<>();


    public static double check(Connection con, int index, double deposit) throws SQLException {
        String query = "SELECT Deposit FROM [Order] WHERE [Order].ID = " + index;
        ResultSet rs = viewTable(con, query);
        List<Double> val = new ArrayList<>();
        while(rs.next()) {
            val.add(rs.getDouble("Deposit"));
        }
        double newPayment = val.get(0);
        return newPayment;
    }
    public static void readDeposit(Connection con) throws IOException, SQLException {
        Scanner scan = new Scanner(new File(".\\OrderPayment.csv"));
        String delimiter = ",";
        int index = 0;
        double oldValue = 0.00;
        double newResult = 0.00;
        List<String> sList = new ArrayList<>();
        while (scan.hasNextLine()) {
            sList.add(scan.nextLine());
        }
        Scanner sc = new Scanner(new File(".\\OrderPayment.csv"));
        parse = new String[sList.size()][3];
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            parse[index] = line.split(delimiter);
            oldValue = Double.parseDouble(parse[index][2]);
            oldValues.add(oldValue);
            newResult = check(con, Integer.parseInt(parse[index][0]), oldValue);
            newValues.add(newResult);
            index++;
        }
        System.out.println(newValues);
        System.out.println(oldValues);
        double payment;
        for(int i = 0; i < newValues.size(); i++) {
            if(newValues.get(i) != oldValues.get(i)) {
                payment = newValues.get(i) - oldValues.get(i);
                if(payment != 0) {
                    //eSendPayment(payment);
                    System.out.println("Layaway Payment Sent - Test");
                    OrderPaymentCreate.start(con);
                }
            }
        }
    }
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
}
