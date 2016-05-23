package com.wshibiao.myweather.ui.choosecity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wshibiao.myweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsb on 2016/5/22.
 */
public class MyGridViewAdapter  extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String > list;
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public MyGridViewAdapter(Context ct,List<String> list) {
        inflater = LayoutInflater.from(ct);
        this.list=(list!=null?list:new ArrayList<String>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_hot_city, parent,false);
            holder.id_tv_cityname = (TextView) convertView.findViewById(R.id.hot_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id_tv_cityname.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView id_tv_cityname;
    }

}
