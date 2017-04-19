package com.fyp.faaiz.ets.adapter;

/**
 * Created by zubair on 4/1/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fyp.faaiz.ets.R;

import java.util.ArrayList;

/**
 * Created by zubair on 2/10/17.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MVH> {

    Context myContext;
    ArrayList<String> mdata;
    LayoutInflater inflater;

    public NavigationDrawerAdapter(Context context, ArrayList<String> data) {
        this.myContext = context;
        this.mdata = data;
    }

    @Override
    public NavigationDrawerAdapter.MVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        NavigationDrawerAdapter.MVH mvh = new NavigationDrawerAdapter.MVH(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.MVH holder, int position) {
        holder.title.setText(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class MVH extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public MVH(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title_img);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_row);
        }
    }
}
