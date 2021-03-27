package com.example.roinovel.Novel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownThread extends java.lang.Thread {
    public String url;
    private String con = "";
    private final String title;
    private Type type = null;
    Object parent;

    public String getCon() {
        return con;
    }

    public DownThread(String url, String title,Type type,Object parent) {
        this.url = url;
        this.title = title;
        this.type = type;
        this.parent = parent;
        this.start();
    }

    public DownThread(String url, String title, Type type, boolean f,Object parent) {
        this.url = url;
        this.type = type;
        this.title = title;
        this.parent = parent;
    }

    @Override
    public void run()
    {
        switch (this.type)
        {
            case NewBiQuGe:{
                while (!NewBiQuGe())
                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case ShuQuGe:{
                while (!ShuQuGe())
                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    public boolean NewBiQuGe()
    {
        try {
            URL u = new URL(this.url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML" +
                            ", like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200)
            {
                response.close();
                return false;
            }
            assert response.body() != null;
            Document doc = Jsoup.parse(response.body().string());
            Elements e = doc.getElementsByClass("bookname");
            Element element = doc.getElementById("content");
            con = element.ownText();
            int i;
            con = con.replace(' ','\n');
            i = con.indexOf("亲,点击进去,给个好评呗,分数越高更新越快,据说给新笔趣阁打满分的最后都找到了漂亮的老婆哦! 手机站全新改版" +
                    "升级地址：http://m.xbiquge.la，数据和书签与电脑站同步，无广告清新阅读！");
            if (i!=-1)
                con = con.substring(0,i) + '\n';
            con = this.title + '\n' + con + '\n';
            System.out.println("Get：" + this.title);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean ShuQuGe()
    {
        try {
            URL u = new URL(this.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML" +
                        ", like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (response.code() != 200)
        {
            response.close();
            return false;
        }
        String resp = null;
        try {
            assert response.body() != null;
            resp = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String pattern = "&nbsp;&nbsp;&nbsp;&nbsp;(.*)\\n?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(resp);
        while (m.find())
        {
            String temp = m.group(1);
            int i;
            if ((i = temp.indexOf("<br />")) != -1)
            {
                temp = temp.substring(0,i);
            }
            con = con + temp + '\n';
        }
        con = this.title + '\n' + con + "\n\n";
        ((ShuQuGe_Download)parent).getLen++;
        return true;
    }

}
