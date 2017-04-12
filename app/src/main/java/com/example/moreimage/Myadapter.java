package com.example.moreimage;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by 丁瑞 on 2017/4/13.
 */
class Myadapter extends BaseAdapter {
    private Context context = null;
    private List<MyBitmap> list = new ArrayList<MyBitmap>();
    private int imgId[] = null;
    private LayoutInflater inflater;

    public Myadapter(Context context, List<MyBitmap> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    private class Holder {


        public ImageView item_img;

        public ImageView getItem_img() {
            return item_img;
        }

        public void setItem_img(ImageView item_img) {
            this.item_img = item_img;
        }
    }


    @Override
    public int getCount() {


        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_listview, null);
            holder = new Holder();
            holder.item_img = (ImageView) view.findViewById(R.id.item_img);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        MyBitmap myBitmap = list.get(i);

        holder.item_img.setImageBitmap(myBitmap.getBm());
        return view;
    }


}

