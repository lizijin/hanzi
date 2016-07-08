package com.peter.action;

import java.io.*;

/**
 * Created by jiangbin on 16/7/8.
 */
public class FileUtils {
    public static  void main(String[] args){
        merge("bihua",".sql");
    }
    private static void copyFileToFile(String fromFile, String toFile) {

        try {
            FileInputStream fis = new FileInputStream(fromFile);
            FileOutputStream fos = new FileOutputStream(toFile,true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(fos));
            String temp;
            while ((temp = reader.readLine()) != null) {//一次读一行
                write.write(temp);
                write.write("\n");
            }
            write.flush();

            reader.close();
            write.close();
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File(fromFile).delete();
    }
    public static void merge(final String keyword, final String suffix){
        File file = new File(".");
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains(keyword)&&name.endsWith(suffix);
            }
        });
        for(int i =1;i<files.length;i++){
            copyFileToFile(files[i].getName(),files[0].getName());
        }
        files[0].renameTo(new File(keyword+suffix));
    }
}
