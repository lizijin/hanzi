package com.peter;


import com.peter.action.BihuaParser;
import com.peter.bean.Bihua;
import com.peter.db.SQLiteJDBC;
import com.peter.db.SQLiteJDBC2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by jiangbin on 16/7/4.
 */
public class Crawler {
    static  char low= '\u6432';
    static  char top = '\u9FA5';

    static char middle1 = '\u6967';
    static char middle2 = '\u846e';
    private  static  ThreadLocal<Long> mLastTime = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };
    public static void main(String[] args) throws IOException {

        CrawlerThread thread1 = new CrawlerThread(low, middle1,"bihua2.db");
        thread1.setName("thread1");

        CrawlerThread thread2 = new CrawlerThread('\u6968', middle2,"bihua3.db");
        thread1.setName("thread2");
        CrawlerThread thread3 = new CrawlerThread('\u846f', top,"bihua4.db");
        thread1.setName("thread3");

        thread1.start();
        thread2.start();
        thread3.start();
    }
    static class CrawlerThread extends Thread {
        char start;
        char end;
        SQLiteJDBC sqLiteJDBC2;
        public CrawlerThread(char start, char end,String dbName) {
            this.start = start;
            this.end = end;
            mLastTime.set(System.currentTimeMillis());
            sqLiteJDBC2=  new SQLiteJDBC(dbName);
        }

        @Override
        public void run() {

            try {
                for (char c = start; c <= end; c++) {
                    if ((c - start) % 100 == 0) {
                        System.out.println(Thread.currentThread().getName()+" 完成" + (c - start) * 100.f / (end - start) + "% 花时间" + (System.currentTimeMillis() - mLastTime.get()));
                        mLastTime.set(System.currentTimeMillis());
                    }
                    String lowString =""+c;
                    String encode = URLEncoder.encode(lowString).replace("%","");
                    String post = "http://bihua.51240.com/web_system/51240_com_www/system/file/bihua/get_0/?font="+encode+"&shi_fou_zi_dong=1&cache_sjs1=15123011";

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(post);
                    HttpResponse response =httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    String s = EntityUtils.toString(entity);
                    Bihua bihua = BihuaParser.parseBihua(s,encode);
                    sqLiteJDBC2.insert(bihua);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
