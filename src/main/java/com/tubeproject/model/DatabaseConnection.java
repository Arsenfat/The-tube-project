package com.tubeproject.model;


import java.sql.*;

public class DatabaseConnection {

    private static final String dbPath = "jdbc:mysql://remotemysql.com:3306/5qFaDYUMfJ?connectTimeout=5000&socketTimeout=30000";
    private static final String user = "5qFaDYUMfJ";
    private static final String password = "J7R1UyqPYh";
    private static Connection con = null;
    private static PreparedStatement stm = null;
    private static ResultSet res = null;
    private static int erreur;

    public static Exception DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbPath, user, password);
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("select * from stations");
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                System.out.println(rs.getString(1));
            con.close();

            return  null;

        }
        catch (Exception e)
        {
            System.out.println(e);

            return e;
        }
    }

    public static void DatabaseOpen() throws SQLException {
        Connection con = DriverManager.getConnection(dbPath, user, password);
        con.setAutoCommit(false);
        DatabaseConnection.con = con;
    }


    public static int DatabaseClose() throws SQLException {
        //stm.close();
        con.close();

        return erreur;
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        DatabaseConnection.con = con;
    }

    public static Statement getStm() {
        return stm;
    }

    public static void setStm(PreparedStatement stm) {
        DatabaseConnection.stm = stm;
    }

    public static ResultSet getRes() {
        return res;
    }

    public static void setRes(ResultSet res) {
        DatabaseConnection.res = res;
    }

    public static PreparedStatement prepareStmt(String statement) throws SQLException {

        return con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
    }
}
