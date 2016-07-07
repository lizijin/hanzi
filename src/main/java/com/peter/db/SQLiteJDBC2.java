package com.peter.db;

/**
 * Created by jiangbin on 16/7/7.
 */

import com.peter.bean.Bihua;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteJDBC2
{

    public  Connection connection;
    public  Statement statement;



    public SQLiteJDBC2(String dbname){
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:"+dbname);
                connection.setAutoCommit(false);
                statement = connection.createStatement();
                createTable();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();
            }
    }
    public  void  createTable(){
        String sql ="CREATE TABLE IF NOT EXISTS AIMATION ("+
                "ENCODE TEXT ,"+
                "HANZI TEXT ,"+
                "ANIMATION TEXT)";
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
     * @param
     */
    public  void insert(String hanzi,String content,String encode){
        String sql = "INSERT INTO AIMATION(HANZI,ANIMATION,ENCODE) values ('"+hanzi+"','"+content+"','"+encode+"');";
        System.out.println(sql);

        try {
            statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
