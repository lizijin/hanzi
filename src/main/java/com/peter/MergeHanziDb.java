package com.peter;

import com.peter.db.BihuaDbOperate;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;

/**
 * Created by jiangbin on 16/7/7.
 */
public class MergeHanziDb {

    public Connection fromConnection;
    public Statement fromStatement;

    public Connection toConnection;
    public Statement toStatement;
    public String formDbName;

    public static void main(String[] args) {
        new MergeHanziDb("bihua4.db", "bihua.db").copyLineByLine();
    }

    public static void merge(){
        File file = new File(".");
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains("bihua")&&name.endsWith(".db");
            }
        });
        for(int i =1;i<files.length;i++){
            new MergeHanziDb(files[i].getName(),files[0].getName()).copyLineByLine();
        }
        files[0].renameTo(new File("bihua.db"));
    }
    public MergeHanziDb(String fromDb, String toDb) {
        try {
            this.formDbName = fromDb;
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
                    String HANZI = rs.getString(BihuaDbOperate.HANZI);
                    String BIHUACOUNT = rs.getString(BihuaDbOperate.BIHUACOUNT);
                    String BIHUASTEP = rs.getString(BihuaDbOperate.BIHUASTEP);
                    String ENCODE = rs.getString(BihuaDbOperate.ENCODE);
                    String PINYIN = rs.getString(BihuaDbOperate.PINYIN);
                    String POINTS = rs.getString(BihuaDbOperate.POINTS);
                insert(HANZI, BIHUACOUNT, BIHUASTEP, ENCODE, PINYIN, POINTS);

                }
                if (!find) break;
                else offset += 100;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        new File(formDbName).delete();
        System.out.println("exit "+(System.currentTimeMillis()-beginTime));
    }

    public void insert(String HANZI, String BIHUACOUNT, String BIHUASTEP, String ENCODE, String PINYIN, String POINTS) {
        String sql = "INSERT INTO  HANZI (HANZI," + BihuaDbOperate.ENCODE + "," + BihuaDbOperate.BIHUASTEP + "," + BihuaDbOperate.PINYIN + "," + BihuaDbOperate.POINTS + ") values ('" + HANZI + "','" + ENCODE + "','" + BIHUASTEP + "','" + PINYIN + "','" + POINTS + "');";
        System.out.println(sql);
        try {
            toStatement.executeUpdate(sql);
            toConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
