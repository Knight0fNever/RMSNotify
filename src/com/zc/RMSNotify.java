package com.zc;

import java.io.IOException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import static com.zc.Email.*;


public class RMSNotify {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        String sqlHost = "jdbc:sqlserver://JOSH-IT\\SQLEXPRESS;databaseName=SF Golden Adventures";
        String uName = "sa";
        String uPass = "Zcsf4119!";
        Connection con = DriverManager.getConnection(sqlHost, uName, uPass);
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        File fac = new File(".\\JavaNotifier.txt");
        if (!fac.exists()) {
            fac.createNewFile();
            Filer fl = new Filer();
            int maxTransNumber = maxTransNum(con);
            fl.writeTrans(maxTransNumber);
        }
        File fac2 = new File(".\\JavaOrderNotifier.txt");
        if (!fac2.exists()) {
            fac2.createNewFile();
            Filer fl = new Filer();
            int maxOrderNumber = maxOrderNumber(con);
            fl.writeTrans(maxOrderNumber);
        }
        File fac3 = new File(".\\OrderPayment.csv");
        if(!fac3.exists()) {
            OrderPaymentCreate.start(con);
        }
        OrderPaymentCheck.readDeposit(con);
        executeTrans(con);
        executeOrder(con);
    }

    public static void executeTrans(Connection con) {
        try {
            Filer fl = new Filer();
            int oldTransNumber = fl.readTrans();
            System.out.println(oldTransNumber);
            int transNumber = maxTransNum(con);
            System.out.println(transNumber);
            //Checks for new transaction number
            if(transNumber > oldTransNumber) {
                Filer wr = new Filer();
                wr.writeTrans(transNumber);
                //Gets new transaction information from DB
                String query = "SELECT [Transaction].TransactionNumber, Item.[Description], TransactionEntry.Price FROM [Transaction] " +
                "LEFT JOIN TransactionEntry ON [Transaction].TransactionNumber = TransactionEntry.TransactionNumber " +
                "LEFT JOIN Item ON TransactionEntry.ItemID = Item.ID " +
                "WHERE [Transaction].TransactionNumber = " + transNumber;
                List<Double> price = new ArrayList<Double>();
                List<String> item = new ArrayList<String>();
                ResultSet rs = viewTable(con, query);
                while (rs.next()) {
                    item.add(rs.getString("Description"));
                    price.add(rs.getDouble("Price"));
                    transNumber = rs.getInt("TransactionNumber");
                }
                double total = 0.00;
                for(int i = 0; i < price.size(); i++) {
                    total += price.get(i);
                }
                System.out.println("Transaction: " + transNumber);
                System.out.println("Item: " + item);
                System.out.println("Price: " + price);
                System.out.println("Total: " + total);
                if(total >= 2000.00) {
                    //eSend(total, item);
                    System.out.println("Test email complete - Sale");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeOrder(Connection con) {
        try {
            Filer fl = new Filer();
            int oldOrderNumber = fl.readOrder();
            System.out.println(oldOrderNumber);
            int orderNumber = 0;
            int newOrderNumber = maxOrderNumber(con);
            System.out.println(newOrderNumber);
            //Checks for new order number
            if(newOrderNumber > oldOrderNumber) {
                orderNumber = newOrderNumber;
                int orderType = 0;
                double orderDeposit = 0.00;
                List<Double> price = new ArrayList<Double>();
                List<String> item = new ArrayList<String>();
                Filer wr = new Filer();
                wr.writeOrder(orderNumber);
                //Gets new order information from DB
                String query = "SELECT [Order].ID, [Order].[Type], [Order].Deposit, [OrderEntry].Price, OrderEntry.[Description] FROM [Order] " +
                "LEFT JOIN OrderEntry ON [Order].ID = OrderEntry.OrderID WHERE [Order].ID = " + orderNumber;
                ResultSet rs = viewTable(con, query);
                while (rs.next()) {
                    item.add(rs.getString("Description"));
                    price.add(rs.getDouble("Price"));
                    orderType = rs.getInt("Type");
                    orderDeposit = rs.getDouble("Deposit");
                }
                double total = 0.00;
                for(int i = 0; i < price.size(); i++) {
                    total += price.get(i);
                }
                System.out.println(item);
                System.out.println(price);
                System.out.println(total);
                System.out.println(orderDeposit);
                if((total >= 2000.00) && orderType == 5) {
                    //eSendOrder(total, orderDeposit, item);
                    System.out.println("Test email complete - Layaway");
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
            String query = "DECLARE @transNumber int SELECT @transNumber = MAX(TransactionNumber) FROM [Transaction] " +
                    "SELECT TransactionNumber FROM [Transaction] WHERE TransactionNumber = @transNumber";
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

    public static int maxOrderNumber(Connection con) {
        Statement stmt = null;
        int result = 0;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String query = "DECLARE @orderNumber int SELECT @orderNumber = MAX(ID) FROM [Order]" +
            "SELECT ID FROM [Order] WHERE ID = @orderNumber";
            ResultSet rs = viewTable(con, query);
            while (rs.next()) {
                result = rs.getInt("ID");
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
}


