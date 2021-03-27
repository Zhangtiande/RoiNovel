package com.example.roinovel.Novel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Search {
    private final String NovelName;
    private final OkHttpClient client = new OkHttpClient();
    public HashMap<String,List<Novel>> SearchResults;
    private Type type = null;


    public Search(String name, Type type) throws MalformedURLException {
        this.NovelName = name;
        this.SearchResults = new HashMap<String, List<Novel>>();
        this.type = type;
    }

    /**
     * @throws IOException
     * 新笔趣阁获取小说
     */
    /*
    最后更新：      <td class="odd".*>(.*)</a>
    小说url、名字： <td class="even"><a href="(.*)" target="_blank">(.*)</a><
    作者：         <td class="even">([\u4e00-\u9fa5]+)</td>
    章节：         <dd><a href='(.*)' >(.*)</a></dd>
    正文：         &nbsp;&nbsp;&nbsp;&nbsp;(.*)\n?
            while (m.find())
        {
            String temp = m.group(1);
            temp = '\n' + temp;
            int i;
            if ((i = temp.indexOf("<br />")) != -1)
            {
                temp = temp.substring(0,i);
            }
            str.append(temp);
        }
    */
    public void NewBiQuGe() throws IOException {
        ArrayList<Novel> NovelInfo = new ArrayList<Novel>();
        URL url = new URL("http://www.xbiquge.la/modules/article/waps.php");
        RequestBody fromBody = new FormBody.Builder()
                .add("searchkey", NovelName)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML" +
                        ", like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .post(fromBody)
                .build();
        Response response = client.newCall(request).execute();
        Document doc =  Jsoup.parse(response.body().string());
        Elements Headlines = doc.getElementsByTag("tr");
        Headlines.remove(0);
        for (Element s: Headlines)
        {
            Novel temp = new Novel();
            Elements elements = s.getElementsByClass("even");
            temp.setName(elements.get(0).text());
            temp.setAuthor(elements.get(1).text());
            temp.setLastUpdate(s.getElementsByClass("odd").text());
            temp.setUrl(s.getElementsByTag("a").attr("href"));
            NovelInfo.add(temp);
        }
        SearchResults.put("新笔趣阁",NovelInfo);
    }

    /**
     * @throws IOException
     * 书趣阁获取小说
     */
    public void ShuQuGe() throws IOException {
        ArrayList<Novel> NovelInfo = new ArrayList<Novel>();

        URL url = new URL("http://www.shuquge.com/search.php");
        RequestBody fromBody = new FormBody.Builder()
                .add("searchkey", NovelName)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML" +
                        ", like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .post(fromBody)
                .build();
        Response response ;
        response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Elements Headlines = doc.getElementsByClass("bookbox");
        for (Element s: Headlines)
        {
            Novel temp = new Novel();
            Elements elements = s.getElementsByClass("bookname");
            temp.setName(elements.get(0).text());
            String pre_url = "http://www.shuquge.com";
            temp.setUrl(pre_url + s.getElementsByTag("a").get(0).attr("href"));
            temp.setLastUpdate(s.getElementsByTag("a").get(1).text());
            elements = s.getElementsByClass("author");
            temp.setAuthor(elements.get(0).text().substring(3));
            NovelInfo.add(temp);
        }
        SearchResults.put("书趣阁",NovelInfo);
    }


    public void search() throws IOException {
        switch (this.type)
        {
            case ShuQuGe:{
                ShuQuGe();
                break;
            }
            case NewBiQuGe:{
                NewBiQuGe();
                break;
            }
        }
    }

}
