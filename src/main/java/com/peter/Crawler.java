package com.peter;


import com.peter.action.BihuaParser;
import com.peter.action.FileUtils;
import com.peter.bean.Bihua;
import com.peter.db.BihuaDbOperate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jiangbin on 16/7/4.
 */
public class Crawler {

    private  static  ThreadLocal<Long> mLastTime = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };
    public static void main(String[] args) throws IOException {

        //清理掉bihua*.db 和bihua*.sql

        File file = new File(".");
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains("bihua")&&(name.endsWith(".db")||name.endsWith(".sql"));
            }
        });
        for(File deleteFile:files ){
            deleteFile.delete();
        }
        System.out.println("清理完毕");
        CountDownLatch countDownLatch = new CountDownLatch(6);
//        CrawlerThread thread4 = new CrawlerThread('\u4E00', '\u4fff', 4,countDownLatch);
//        thread4.setName("thread4");
//        CrawlerThread thread5 = new CrawlerThread('\u5000', '\u5fff', 5,countDownLatch);
//        thread5.setName("thread5");
//        CrawlerThread thread6 = new CrawlerThread('\u6000', '\u6fff', 6,countDownLatch);
//        thread6.setName("thread6");
//        CrawlerThread thread7 = new CrawlerThread('\u7000', '\u7fff', 7,countDownLatch);
//        thread7.setName("thread7");
//        CrawlerThread thread8 = new CrawlerThread('\u8000', '\u8fff', 8,countDownLatch);
//        thread8.setName("thread8");
//        CrawlerThread thread9 = new CrawlerThread('\u9000', '\u9FA5', 9,countDownLatch);
//        thread9.setName("thread9");

        CrawlerThread thread4 = new CrawlerThread('\u4E00', '\u4E0f', 4,countDownLatch);
        thread4.setName("thread4");
        CrawlerThread thread5 = new CrawlerThread('\u5000', '\u500f', 5,countDownLatch);
        thread5.setName("thread5");
        CrawlerThread thread6 = new CrawlerThread('\u6000', '\u600f', 6,countDownLatch);
        thread6.setName("thread6");
        CrawlerThread thread7 = new CrawlerThread('\u7000', '\u700f', 7,countDownLatch);
        thread7.setName("thread7");
        CrawlerThread thread8 = new CrawlerThread('\u8000', '\u800f', 8,countDownLatch);
        thread8.setName("thread8");
        CrawlerThread thread9 = new CrawlerThread('\u9000', '\u900f', 9,countDownLatch);
        thread9.setName("thread9");
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("轮到老板我来干活了");
        FileUtils.merge("bihua",".sql");
        MergeHanziDb.merge();

    }
    static class CrawlerThread extends Thread {
        char start;
        char end;
        BihuaDbOperate bihuaDbOperate;
        CountDownLatch countDownLatch;
        HttpClient httpClient = new DefaultHttpClient();

        public CrawlerThread(char start, char end,int sequence,CountDownLatch countDownLatch) {
            this.start = start;
            this.end = end;
            mLastTime.set(System.currentTimeMillis());
            bihuaDbOperate =  new BihuaDbOperate("bihua"+sequence+".db","bihua"+sequence+".sql");
            this.countDownLatch = countDownLatch;
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

                    HttpGet httpGet = new HttpGet(post);
                    HttpResponse response =httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    String s = EntityUtils.toString(entity);
                    Bihua bihua = BihuaParser.parseBihua(s,encode);
                    bihuaDbOperate.insert(bihua);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            bihuaDbOperate.closeResource();
            this.countDownLatch.countDown();
        }
    }
}
