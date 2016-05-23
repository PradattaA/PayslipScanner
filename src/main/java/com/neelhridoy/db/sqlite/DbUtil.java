package com.neelhridoy.db.sqlite;

import com.neelhridoy.pdf.scanner.payslip.PayslipModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Palash
 * Date: 12/5/16
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtil {
    private static final String DB_PATH = "salary.db";
    private static Connection connection;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null || connection.isClosed()) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        }
        return connection;
    }

    public static Statement getStatement() throws SQLException, ClassNotFoundException {
        Statement statement = getConnection().createStatement();
        statement.setQueryTimeout(30);
        return statement;
    }

    public static int executeUpdate(String sql) throws SQLException, ClassNotFoundException {
        System.out.println("sql = " + sql);
        return getStatement().executeUpdate(sql);
    }

    public static ResultSet getResultSet(String sql) throws SQLException, ClassNotFoundException {
        System.out.println("sql = " + sql);
        return getStatement().executeQuery(sql);
    }

    public static void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static int createTable() throws SQLException, ClassNotFoundException {
        String sql = "create table if not exists payslip(" +
                "designation string, " +
                "month string, " +
                "year integer, " +
                "basic integer, " +
                "hra integer, " +
                "ca integer, " +
                "cca integer, " +
                "totalEarning integer, " +
                "pf integer, " +
                "pTax integer, " +
                "iTax integer, " +
                "otherDeduction integer, " +
                "totalDeduction integer, " +
                "net integer " +
                ")";
        return executeUpdate(sql);
    }


    public static void store(List<PayslipModel> payslipList) throws SQLException, ClassNotFoundException {
        try {
            for (PayslipModel payslipModel : payslipList) {
                String insertSql = getInsertSql(payslipModel);
                DbUtil.executeUpdate(insertSql);
            }
        } finally {
            DbUtil.close();
        }
    }

    public static String getInsertSql(PayslipModel paySlip) {
        String sql = "INSERT INTO PAYSLIP (" +
                "designation, " +
                "month, " +
                "year, " +
                "basic, " +
                "hra, " +
                "ca, " +
                "cca, " +
                "totalEarning, " +
                "pf, " +
                "pTax, " +
                "iTax, " +
                "otherDeduction, " +
                "totalDeduction, " +
                "net \n" +
                ") VALUES (" +
                "'" + paySlip.getDesignation() + "'," +
                "'" + paySlip.getMonth() + "'," +
                "" + paySlip.getYear() + "," +
                "" + paySlip.getBasicEarning() + "," +
                "" + paySlip.getHra() + "," +
                "" + paySlip.getConveyanceAllowance() + "," +
                "" + paySlip.getCca() + "," +
                "" + paySlip.getTotalEarnings() + "," +
                "" + paySlip.getpFund() + "," +
                "" + paySlip.getpTax() + "," +
                "" + paySlip.getiTax() + "," +
                "" + paySlip.getOther() + "," +
                "" + paySlip.getTotalDeduction() + "," +
                "" + paySlip.getNetPayable() + "" +
                ")";
        return sql;
    }

    public static List<PayslipModel> fetchPaySlips() throws SQLException, ClassNotFoundException {
        List<PayslipModel> payslipModelList = new ArrayList<>();
        ResultSet resultSet = DbUtil.getResultSet("SELECT * FROM PAYSLIP");
        while (resultSet.next()) {
            PayslipModel payslipModel = new PayslipModel();
            payslipModel.setDesignation(resultSet.getString("designation"));
            payslipModel.setMonth(resultSet.getString("month"));
            payslipModel.setYear(resultSet.getString("year"));
            payslipModel.setBasicEarning(resultSet.getInt("basic"));
            payslipModel.setHra(resultSet.getInt("hra"));
            payslipModel.setConveyanceAllowance(resultSet.getInt("ca"));
            payslipModel.setCca(resultSet.getInt("cca"));
            payslipModel.setTotalEarnings(resultSet.getInt("totalEarning"));
            payslipModel.setpFund(resultSet.getInt("pf"));
            payslipModel.setpTax(resultSet.getInt("pTax"));
            payslipModel.setiTax(resultSet.getInt("iTax"));
            payslipModel.setOther(resultSet.getInt("otherDeduction"));
            payslipModel.setTotalDeduction(resultSet.getInt("totalDeduction"));
            payslipModel.setNetPayable(resultSet.getInt("net"));

            payslipModelList.add(payslipModel);
        }
        return payslipModelList;
    }

}
