package com.pardus.kdictionary;

import android.content.Context;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2015/5/28.
 */
public class DefinitionListAdapter extends BaseAdapter {
    private  ArrayList<HashMap<String,String>> data;
    private  Context mContext;
    public DefinitionListAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.data = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_definition,null,false);
            holder.definition = (TextView) convertView.findViewById(R.id.tv_definition);
            holder.searchTarget = (TextView) convertView.findViewById(R.id.tv_search_target);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = data.get(position);
        String key="";
        String value ="";
        for(Map.Entry<String,String> s: map.entrySet()){
            key =s.getKey();
            value= s.getValue();
        }
        holder.definition.setText(value);
        holder.searchTarget.setText(key);
        return convertView;

    }

    static class ViewHolder{
        TextView definition;
        TextView searchTarget;
    }
}
