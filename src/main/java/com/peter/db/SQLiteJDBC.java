package com.peter.db;

/**
 * Created by jiangbin on 16/7/7.
 */

import com.peter.bean.Bihua;

import java.sql.*;

public class SQLiteJDBC {

    public static final String HANZI = "HANZI";
    public static final String ENCODE = "ENCODE";
    public static final String BIHUACOUNT = "BIHUACOUNT";
    public static final String BIHUASTEP = "BIHUASTEP";
    public static final String PINYIN = "PINYIN";
    public static final String POINTS = "POINTS";
    public Connection connection;
    public Statement statement;

    public SQLiteJDBC(String dbName) {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            createTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + HANZI + " (" +
                HANZI + " TEXT," +
                ENCODE + " TEXT ," +
                BIHUACOUNT + " INTEGER ," +
                BIHUASTEP + " TEXT ," +
                PINYIN + " TEXT ," +
                POINTS + " TEXT)";
        System.out.println(sql);
        try {
            statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 插入数据
     *
     * @param bihua
     */
    public void insert(Bihua bihua) {
        String sql = "INSERT INTO " + HANZI + "(HANZI," + ENCODE + "," + BIHUASTEP + "," + PINYIN + "," + POINTS + ") values ('" + bihua.hanzi + "','" + bihua.encode + "','" + bihua.bihuaStep + "','" + bihua.pinyin + "','" + bihua.points + "');";
        System.out.println(sql);
        try {
            statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
