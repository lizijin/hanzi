package com.peter;

import com.peter.db.SQLiteJDBC;

import java.sql.*;

/**
 * Created by jiangbin on 16/7/7.
 */
public class MergeHanziDb {

    public Connection fromConnection;
    public Statement fromStatement;

    public Connection toConnection;
    public Statement toStatement;

    public static void main(String[] args) {
        new MergeHanziDb("bihua4.db", "bihua.db").copyLineByLine();
    }

    public MergeHanziDb(String fromDb, String toDb) {
        try {
            Class.forName("org.sqlite.JDBC");
            fromConnection = DriverManager.getConnection("jdbc:sqlite:" + fromDb);
            fromConnection.setAutoCommit(false);
            fromStatement = fromConnection.createStatement();

            toConnection = DriverManager.getConnection("jdbc:sqlite:" + toDb);
            toConnection.setAutoCommit(false);
            toStatement = toConnection.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void copyLineByLine() {
        long beginTime = System.currentTimeMillis();

        int offset = 0;

        while (true) {
            try {
                String sql = "select * from HANZI limit 100 offset " + offset + ";";
                System.out.println(sql);
                ResultSet rs = fromStatement.executeQuery(sql);
                boolean find = false;
                while (rs.next()) {
                    find = true;
                    String HANZI = rs.getString(SQLiteJDBC.HANZI);
                    String BIHUACOUNT = rs.getString(SQLiteJDBC.BIHUACOUNT);
                    String BIHUASTEP = rs.getString(SQLiteJDBC.BIHUASTEP);
                    String ENCODE = rs.getString(SQLiteJDBC.ENCODE);
                    String PINYIN = rs.getString(SQLiteJDBC.PINYIN);
                    String POINTS = rs.getString(SQLiteJDBC.POINTS);
                insert(HANZI, BIHUACOUNT, BIHUASTEP, ENCODE, PINYIN, POINTS);

                }
                if (!find) break;
                else offset += 100;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("exit "+(System.currentTimeMillis()-beginTime));
    }

    public void insert(String HANZI, String BIHUACOUNT, String BIHUASTEP, String ENCODE, String PINYIN, String POINTS) {
        String sql = "INSERT INTO  HANZI (HANZI," + SQLiteJDBC.ENCODE + "," + SQLiteJDBC.BIHUASTEP + "," + SQLiteJDBC.PINYIN + "," + SQLiteJDBC.POINTS + ") values ('" + HANZI + "','" + ENCODE + "','" + BIHUASTEP + "','" + PINYIN + "','" + POINTS + "');";
        System.out.println(sql);
        try {
            toStatement.executeUpdate(sql);
            toConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void delete(String ENCODE) {
//        String sql = "DELETE from HANZI where ENCODE ='" + ENCODE + "'";
//        try {
//            fromStatement.executeUpdate(sql);
//            fromConnection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
