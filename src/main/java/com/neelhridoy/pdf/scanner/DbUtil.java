package com.neelhridoy.pdf.scanner;

import java.sql.*;

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
}
