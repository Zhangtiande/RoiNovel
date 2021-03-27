package com.example.roinovel.Novel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Download {
    protected final String url;   //小说网址
    protected final String name;  //小说名字
    protected final Map<Integer,String> list = new HashMap<Integer,String>();         //小说章节url列表
    protected final Map<Integer,String> Content = new HashMap<Integer,String>();      //用于存放小说正文
    protected final Map<Integer,String> TitleList = new HashMap<Integer,String>();    //小说章节名称列表
    protected final ArrayList<Integer> fail = new ArrayList<Integer>();        //用于存放爬取失败章节的列表
    public int count = 0;

    public Download(String url, String name) throws MalformedURLException {
        this.url = url;
        this.name = name;
    }

    public Map<Integer, String> getContent() {
        return Content;
    }

    public abstract void ListGet();//获取小说章节url列表

    public abstract void download();//多线程爬取小说正文

    public void contentWrite() throws FileNotFoundException {
        File file = new File(this.name + ".txt");
        PrintWriter in = new PrintWriter(file);
        for (Integer i : Content.keySet())
        {
            in.print(Content.get(i));
        }
        in.close();
    }
}
