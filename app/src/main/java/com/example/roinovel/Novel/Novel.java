package com.example.roinovel.Novel;

import java.net.MalformedURLException;

public class Novel {
    public String Name=null;
    public String Author=null;
    public String LastUpdate=null;
    public String url;


    public void setName(String name) {
        Name = name;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "小说名：" + this.Name + "\n作者：" + this.Author + "\n最后更新：" + this.LastUpdate + "\n小说网址：" +
                this.url + "\n";
    }

    public String string()
    {
        return "小说名：" + this.Name + "\n作者：" + this.Author + "\n最后更新：" + this.LastUpdate + "\n" ;
    }

    public void setUrl(String url) throws MalformedURLException {
        this.url = url;
    }
}
