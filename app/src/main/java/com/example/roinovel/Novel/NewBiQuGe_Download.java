package com.example.roinovel.Novel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewBiQuGe_Download extends Download{

    public NewBiQuGe_Download(String url, String name) throws MalformedURLException {
        super(url,name);
        this.ListGet();
    }

    @Override
    /*
    * 小说url列表获取
    * */
    public void ListGet()
    {
        try {
            Document doc = Jsoup.connect(this.url).get();
            Elements elements = doc.getElementsByTag("dd");
            for (Element s: elements)
            {
                String temp = s.getElementsByTag("a").attr("href");
                TitleList.put(count,s.getElementsByTag("a").text());
                list.put(count++,"http://www.xbiquge.la" + temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    /*
    *多线程爬取小说正文
    * */
    public void download() {
        ExecutorService fixedThreadPoll = Executors.newFixedThreadPool(64);
        ArrayList<DownThread> threads = new ArrayList<DownThread>();
        for (Integer i: list.keySet())
        {
            DownThread temp = new DownThread(list.get(i),TitleList.get(i), Type.NewBiQuGe,false);
            fixedThreadPoll.execute(temp);
            try {
                temp.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threads.add(temp);
        }
        fixedThreadPoll.shutdown();
        while (!fixedThreadPoll.isTerminated())
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int i=0;
        for (DownThread t: threads)
        {
            Content.put(i++,t.getCon());
        }
        try {
            contentWrite();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        try {
//            long startTime = System.currentTimeMillis();    //获取开始时间
//            NewBiQuGe_Download n = new NewBiQuGe_Download("http://www.xbiquge.la/0/813/","同桌凶猛");
//            n.download();
//            long endTime = System.currentTimeMillis();    //获取结束时间
//
//            System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }
}
