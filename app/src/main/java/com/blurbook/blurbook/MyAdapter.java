package com.blurbook.blurbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Hoang Nguyen on 2/3/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
//    List<DrawerRowDetail> rows = Collections.emptyList();

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java


//    public MyAdapter(Context context, List<DrawerRowDetail> rows){
//        inflater=LayoutInflater.from(context);
//        this.rows = rows;
//    }

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
//        DrawerRowDetail current = rows.get(position);
//        holder.title.setText(current.title);
//        holder.icon.setImageResource(current.iconId);

        holder.textView.setText(mNavTitles[position]);
        holder.imageView.setImageResource(mIcons[position]);
    }

    @Override
    public int getItemCount() {
//        return mNavTitles.size();
        return mNavTitles.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        TextView title;
//        ImageView icon;

        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.rowText);
            imageView = (ImageView) itemView.findViewById(R.id.rowIcon);


        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "You hit item " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
