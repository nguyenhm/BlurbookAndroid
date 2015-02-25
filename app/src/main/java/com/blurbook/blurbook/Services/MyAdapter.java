package com.blurbook.blurbook.Services;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blurbook.blurbook.R;

/**
 * Created by Hoang Nguyen on 2/3/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];        // Int Array to store the passed icons resource value from MainActivity.java

    public MyAdapter(Context context, String Titles[], int Icons[]){
        this.context = context;
        inflater=LayoutInflater.from(context);
        this.mNavTitles = Titles;
        this.mIcons = Icons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_drawer_item, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(mNavTitles[position]);
        holder.imageView.setImageResource(mIcons[position]);
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.rowText);
            imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
        }
    }
}
