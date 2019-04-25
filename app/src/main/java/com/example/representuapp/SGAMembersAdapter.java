package com.example.representuapp;

import java.util.ArrayList;

import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;


public class SGAMembersAdapter extends RecyclerView.Adapter<SGAMembersAdapter.SingleItemRowHolder> {

    private ArrayList<SGAMember> itemsList;
    private Activity mContext;
    int layout;

    public SGAMembersAdapter(Activity context, ArrayList<SGAMember> itemsList, int layout) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layout = layout;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        SGAMember item = itemsList.get(i);

        holder.tv_languages.setTag(i);
        holder.tv_languages.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tv_languages;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tv_languages = (TextView) view.findViewById(R.id.tv_languages);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int position = Integer.parseInt(tv_languages.getTag().toString());

                    Toast.makeText(v.getContext(), tv_languages.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}