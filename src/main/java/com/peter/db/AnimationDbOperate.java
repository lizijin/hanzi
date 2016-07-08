package com.peter.db;

/**
 * Created by jiangbin on 16/7/7.
 */

import com.peter.bean.Bihua;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AnimationDbOperate
{

    public  Connection connection;
    public  Statement statement;



    public AnimationDbOperate(String dbname,String fileName){
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:"+dbname);
                connection.setAutoCommit(false);
                statement = connection.createStatement();
                createTable();
                this.mFileWriter = new FileWriter(fileName,true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
    }

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
    public  void  createTable(){
        String sql ="CREATE TABLE IF NOT EXISTS ANIMATION ("+
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
        String sql = "INSERT INTO ANIMATION(HANZI,ANIMATION,ENCODE) values ('"+hanzi+"','"+content+"','"+encode+"');";
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
