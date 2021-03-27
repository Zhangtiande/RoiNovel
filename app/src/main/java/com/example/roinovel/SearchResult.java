package com.example.roinovel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roinovel.Novel.Novel;
import com.example.roinovel.Novel.ShuQuGe_Download;
import com.example.roinovel.listview.NovelAdapter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResult extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "SearchResult";
    private static List<Novel> novelArrayList = new ArrayList<>();
    private Novel novel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        novelArrayList = MainActivity.novels;
        NovelAdapter novelAdapter = new NovelAdapter(this,R.layout.novel_item,novelArrayList);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(novelAdapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        novel = novelArrayList.get(position);

        Toast.makeText(this,"你点击了" + novel.Name + "，现在开始下载，请稍等……", Toast.LENGTH_LONG).show();
        progressBar = view.findViewById(R.id.down_process);
        progressBar.setVisibility(View.VISIBLE);
        NovelDownload(novel.url);
    }


    @SuppressLint("HandlerLeak")
    private final Handler mHandlerMessageObj = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1)
            {
                Map<Integer, String> content = (Map<Integer, String>) msg.obj;
                FileOutputStream out;
                BufferedWriter writer = null;
                try {
                    out = openFileOutput(novel.Name + ".txt", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    for (Integer i: content.keySet())
                    {
                        writer.write(content.get(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Toast.makeText(SearchResult.this,"文件写入成功！",Toast.LENGTH_SHORT).show();
            }else if (msg.arg1 == 3){
                progressBar.setMax((Integer) msg.obj);
                progressBar.setProgress(msg.arg2);
            }else{
                Toast.makeText(SearchResult.this,"下载失败，请重试！",Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void NovelDownload(String url) {
        new Thread(() -> {
            Message message = mHandlerMessageObj.obtainMessage();
            try {
                ShuQuGe_Download download = new ShuQuGe_Download(url,novel.Name);
                new Thread(download::download).start();
                while (!download.finish)
                {
                    Message message2 = mHandlerMessageObj.obtainMessage();
                    message2.arg1 = 3;
                    message2.arg2 = download.getLen;
                    message2.obj = download.len;
                    mHandlerMessageObj.sendMessage(message2);
                }
                message.arg1 = 1;
                message.obj = download.getContent();
                mHandlerMessageObj.sendMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                message.arg1 = 0;
                Log.e(TAG, "NovelSearch: Download Failure!" );
            }
        }).start();
    }







}