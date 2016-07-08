package com.peter.db;

/**
 * Created by jiangbin on 16/7/7.
 */

import com.peter.bean.Bihua;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class BihuaDbOperate {

    public static final String HANZI = "HANZI";
    public static final String ENCODE = "ENCODE";
    public static final String BIHUACOUNT = "BIHUACOUNT";
    public static final String BIHUASTEP = "BIHUASTEP";
    public static final String PINYIN = "PINYIN";
    public static final String POINTS = "POINTS";
    public Connection connection;
    public Statement statement;
    public FileWriter mFileWriter;

    public  void appendFile( String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            mFileWriter.write(content);
            mFileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeResource(){
        if(this.mFileWriter!=null){
            try {
                this.mFileWriter.flush();
                this.mFileWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        if(this.statement!=null){
            try {
                this.statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(this.connection!=null){
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public BihuaDbOperate(String dbName,String fileName) {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            createTable();
            mFileWriter = new FileWriter(fileName,true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (IOException e){
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
        appendFile(sql);
        try {
            statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
