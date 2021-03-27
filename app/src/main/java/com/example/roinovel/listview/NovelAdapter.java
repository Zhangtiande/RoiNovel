package com.example.roinovel.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.roinovel.Novel.Novel;
import com.example.roinovel.R;

import java.util.List;

public class NovelAdapter extends ArrayAdapter<Novel>{

    private final int resourceId;

    public NovelAdapter(@NonNull Context context, int resource, @NonNull List<Novel> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }


    @Override
    public View getView(int position, View ConvertView, ViewGroup parent)
    {
        Novel novel = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (ConvertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.novel_name);
            viewHolder.progressBar = view.findViewById(R.id.down_process);
            view.setTag(viewHolder);
        }else{
            view = ConvertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.progressBar.setTag(novel.Name);
//        if (((String)viewHolder.progressBar.getTag()).compareTo(novel.Name) != 0)
//        {
//            viewHolder.progressBar.setVisibility(View.GONE);
//        }
        viewHolder.textView.setText(novel.string());
        return view;
    }





    static class ViewHolder{
        TextView textView;
        ProgressBar progressBar;
    }
}