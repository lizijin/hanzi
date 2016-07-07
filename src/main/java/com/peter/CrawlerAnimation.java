package com.peter;

import com.peter.action.BihuaParser;
import com.peter.bean.Bihua;
import com.peter.db.SQLiteJDBC;
import com.peter.db.SQLiteJDBC2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by jiangbin on 16/7/7.
 */
public class CrawlerAnimation {
    static char low = '\u4E00';
    static char top = '\u9FA5';
    //    static char middle1 = '\u6967';
//    static char middle2 = '\u846e';
    private static ThreadLocal<Long> mLastTime = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static void main(String[] args) throws IOException {
        CrawlerThread thread4 = new CrawlerThread('\u4E00', '\u4fff', "anmation4.db");
        thread4.setName("thread4");
        CrawlerThread thread5 = new CrawlerThread('\u5000', '\u5fff', "anmation5.db");
        thread5.setName("thread5");
        CrawlerThread thread6 = new CrawlerThread('\u6000', '\u6fff', "anmation6.db");
        thread6.setName("thread6");
        CrawlerThread thread7 = new CrawlerThread('\u7000', '\u7fff', "anmation7.db");
        thread7.setName("thread7");
        CrawlerThread thread8 = new CrawlerThread('\u8000', '\u8fff', "anmation8.db");
        thread8.setName("thread8");
        CrawlerThread thread9 = new CrawlerThread('\u9000', '\u9FA5', "anmation9.db");
        thread9.setName("thread9");

        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();

    }

    static class CrawlerThread extends Thread {
        char start;
        char end;
        SQLiteJDBC2 sqLiteJDBC2;
        HttpClient httpClient = new DefaultHttpClient();

        public CrawlerThread(char start, char end, String dbname) {
            this.start = start;
            this.end = end;
            mLastTime.set(System.currentTimeMillis());
            sqLiteJDBC2 = new SQLiteJDBC2(dbname);
        }

        @Override
        public void run() {

            try {
                for (char c = start; c <= end; c++) {
                    if ((c - start) % 100 == 0) {
                        System.out.println(Thread.currentThread().getName() + " 完成" + (c - start) * 100.f / (end - start) + "% 花时间" + (System.currentTimeMillis() - mLastTime.get()));
                        mLastTime.set(System.currentTimeMillis());
                    }
                    String lowString = "" + c;
                    String encode = URLEncoder.encode(lowString).replace("%", "");
                    String post = "http://www.chiguome.com/bishun/bishun.aspx?functionid=10002&word=" + encode;

                    HttpGet httpGet = new HttpGet(post);
                    HttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    String s = EntityUtils.toString(entity);
                    sqLiteJDBC2.insert("" + c, s, encode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
