package com.example.maker.wordapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.maker.wordapplication.R;
import com.example.maker.wordapplication.bean.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class wordListAdapter extends BaseAdapter {
    private List<Map<String,String>> allWords;
    private Context context;
    private int index;
    public ViewHolder selectItem;
    private boolean deleteShowControll ;
    public wordListAdapter(List<Map<String,String>> allWords,Context context,int index,boolean deleteShowControll){
        this.allWords = allWords;
        this.context = context;
        this.index = index;
        this.deleteShowControll = deleteShowControll;
    }

    @Override
    public int getCount() {
        return allWords.size();
    }

    @Override
    public Object getItem(int i) {
        return allWords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.layout_list_dialog_content,null);
            viewHolder.tv_delete = view.findViewById(R.id.tv_delete);
            viewHolder.tv_word = view.findViewById(R.id.tv_word);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_word.setBackgroundResource(R.color.tv_unchoose);
         viewHolder.tv_word.setTextColor(R.color.mainTextColor);

        if(index == i){
            selectItem = viewHolder;
            viewHolder.tv_word.setBackgroundResource(R.color.tv_choose);
            viewHolder.tv_word.setTextColor(Color.WHITE);

        }
        viewHolder.tv_word.setText(allWords.get(i).get("word"));

        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMyTextView.myTextViewClick(i);
            }
        });
        if (deleteShowControll == true){
            viewHolder.tv_delete.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public interface onClickMyTextView {
        public void myTextViewClick(int id);
    }
    public interface onClickWordView{
        public void myWordTextClick(int id);
    }
    public void setOnClickMyTextView(onClickMyTextView onClickMyTextView) {
        this.onClickMyTextView = onClickMyTextView;
    }
    public void setOnClickWordText(onClickWordView  onClickWordView){
        this.onClickWordView = onClickWordView;
    }
    private onClickMyTextView onClickMyTextView;
    private onClickWordView onClickWordView;



}
