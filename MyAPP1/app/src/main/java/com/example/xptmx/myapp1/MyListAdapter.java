package com.example.xptmx.myapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    Context context;
    ArrayList<list_item> list_itemArrayList;
    ViewHolder viewholder;

    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);

            viewholder = new ViewHolder();
            viewholder.content_textView = (TextView) convertView.findViewById(R.id.content_textView);
            viewholder.date_textView = (TextView) convertView.findViewById(R.id.date_textView);
            convertView.setTag(viewholder);
        }
        else {
            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.content_textView.setText(list_itemArrayList.get(position).getContent());
        viewholder.date_textView.setText(list_itemArrayList.get(position).getCreate_date());

        return convertView;
    }

    class ViewHolder{
        TextView content_textView;
        TextView date_textView;
    }
}
