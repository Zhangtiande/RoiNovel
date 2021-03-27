package com.example.roinovel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roinovel.Novel.Novel;
import com.example.roinovel.Novel.Search;
import com.example.roinovel.Novel.Type;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.roinovel";
    public static List<Novel> novels = null;
    private static Intent intent;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SearchNovel(View view) throws IOException {
        intent = new Intent(this,SearchResult.class);
        EditText editText = (EditText)findViewById(R.id.editTextTextPersonName);
        NovelSearch(editText.getText().toString());
        intent.putExtra(EXTRA_MESSAGE, "success");
        Toast.makeText(this,"正在搜索，请稍侯……",Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("HandlerLeak")
    private final Handler mHandlerMessageObj = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1)
            {
                novels = (List<Novel>) msg.obj;
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this,"搜索失败，请重试！",Toast.LENGTH_LONG).show();
            }
        }
    };


    private void NovelSearch(String name) {
        new Thread(() -> {
            Message message = mHandlerMessageObj.obtainMessage();
            try {
                Search s = new Search(name, Type.ShuQuGe);
                s.search();
                message.arg1 = 1;
                message.obj = s.SearchResults.get("书趣阁");
                mHandlerMessageObj.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                message.arg1 = 0;
                Log.d(TAG, "NovelSearch: Search Failure!");
            }
        }).start();
    }

}