package com.peter;

/**
 * Created by jiangbin on 16/7/7.
 */

import com.peter.db.SQLiteJDBC;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;


import com.peter.db.SQLiteJDBC;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by jiangbin on 16/7/7.
 */
public class MergeAnimationDb {

    public Connection fromConnection;
    public Statement fromStatement;

    public Connection toConnection;
    public Statement toStatement;

    public static void main(String[] args) {
        File file = new File(".");
       File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains("anmation");
            }
        });
        for(int i =1;i<files.length;i++){
            new MergeAnimationDb(files[i].getName(),files[0].getName()).copyLineByLine();
        }
//        System.out.println(Arrays.toString(file.list()));
    }

    public MergeAnimationDb(String fromDb, String toDb) {
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
                String sql = "select * from AIMATION limit 100 offset " + offset + ";";
                System.out.println(sql);
                ResultSet rs = fromStatement.executeQuery(sql);
                boolean find = false;
                while (rs.next()) {
                    find = true;
                    String ENCODE = rs.getString("ENCODE");
                    String HANZI = rs.getString("HANZI");
                    String ANIMATION = rs.getString("ANIMATION");
                    insert(HANZI, ANIMATION, ENCODE);

                }
                if (!find) break;
                else offset += 100;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            fromStatement.close();
            fromConnection.close();
            toStatement.close();
            toConnection.close();
        } catch (Exception e) {
        }

        System.out.println("exit " + (System.currentTimeMillis() - beginTime));
    }

    public void insert(String hanzi, String content, String encode) {
        String sql = "INSERT INTO AIMATION(HANZI,ANIMATION,ENCODE) values ('" + hanzi + "','" + content + "','" + encode + "');";
        System.out.println(sql);

        try {
            toStatement.executeUpdate(sql);
            toConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
