
package com.example.representuapp;

import java.util.ArrayList;

import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import android.widget.TextView;
import android.content.Intent;

import android.widget.Toast;


public class SGAMembersAdapter extends RecyclerView.Adapter<SGAMembersAdapter.SingleItemRowHolder> {

    private ArrayList<SGAMember> itemsList;
    private Activity mContext;
    int layout;
    String bio;
    String id;

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

        holder.text.setTag(i);
        holder.text.setText(item.getName());
        holder.text2.setTag(i);
        holder.text2.setText(item.getPosition());
        bio = item.getBio();
        id = item.idNum;

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView text2;



        public SingleItemRowHolder(View view) {
            super(view);

            this.text = (TextView) view.findViewById(R.id.people);
            this.text2 = (TextView) view.findViewById(R.id.position);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SGAMemberActivity.class);
                    String name = text.getText().toString();
                    String position = text2.getText().toString();
                    intent.putExtra("name", name);
                    intent.putExtra("pos", position);
                    intent.putExtra("bio", bio);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);
                }
            });


        }

    }

}