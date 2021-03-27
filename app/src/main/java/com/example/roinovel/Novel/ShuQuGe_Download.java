package com.example.roinovel.Novel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShuQuGe_Download extends Download {

    public int len = 0;
    public int getLen = 0;
    public boolean finish = false;

    public ShuQuGe_Download(String url, String name) throws MalformedURLException {
        super(url, name);
        this.ListGet();
    }


    @Override
    public void ListGet() {
        len = 0;
        getLen = 0;
        String p = "(.*)index.html";
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(this.url);
        String pre_url = null;
        if (m.find())
            pre_url = m.group(1);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML" +
                        ", like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String resp = response.body().string();
            p = "<dd><a href=\"(.*)\">(.*)</a></dd>";
            pattern = Pattern.compile(p);
            m = pattern.matcher(resp);
            int i = 0;
            while (m.find()) {
                if (i < 12) {
                    i++;
                    continue;
                }
                TitleList.put(i - 12, m.group(2));
                list.put((i - 12), pre_url + m.group(1));
                i++;
            }
            len = i-12;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void download() {
        ExecutorService fixedThreadPoll = Executors.newFixedThreadPool(32);
        ArrayList<DownThread> threads = new ArrayList<DownThread>();
        for (Integer i : list.keySet()) {
            DownThread temp = new DownThread(list.get(i), TitleList.get(i), Type.ShuQuGe, false, this);
            fixedThreadPoll.execute(temp);
            threads.add(temp);
        }
        fixedThreadPoll.shutdown();
        while (!fixedThreadPoll.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int i = 0;
        for (DownThread t : threads) {
            Content.put(i++, t.getCon());
        }
        finish = true;
    }

}
