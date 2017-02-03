package com.zc;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.opencsv.*;

public class OrderPaymentCreate {

    public static void start(Connection con)  {
        ResultSet rs = null;
        try {
            rs = results(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            csvWriter(rs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet results(Connection con) throws SQLException {
        String query = "SELECT ID, Total, Deposit FROM [Order] WHERE Closed = 0";
        ResultSet rs;
            rs = viewTable(con, query);
        return rs;
    }

    public static void csvWriter(ResultSet rs) throws IOException, SQLException {
        CSVWriter writer = new CSVWriter(new FileWriter(".\\OrderPayment.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
        boolean includeHeaders = false;
        writer.writeAll(rs, includeHeaders);
        System.out.println("Saved.");
        writer.close();
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
