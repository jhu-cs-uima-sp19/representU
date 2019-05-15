package com.example.representuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IssuesSGAActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Integer yeaNum = 0;
    public Integer nayNum = 0;
    public TextView summary;
    public String title;
    public String idString;
    private DatabaseReference comments = db.getReference().child("comments");
    String name;
    String id;
    public Button yeaButton;
    public Button nayButton;
    ArrayList<Comment> arrayList = new ArrayList<>();
    TextView comUser;
    TextView comText;
    public ListView commentsList;

    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<Comment> objects;

        private class ViewHolder {
            TextView textView1;
            TextView textView2;
        }

        public CustomAdapter(Context context, ArrayList<Comment> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            Log.d("Size", "" + objects.size());
            return objects.size();
        }

        public Comment getItem(int position) {
            Log.d("Positon", "" + position);
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            IssuesSGAActivity.CustomAdapter.ViewHolder holder = null;
            if(convertView == null) {
                holder = new IssuesSGAActivity.CustomAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.comment_textview, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.commentUsr);
                holder.textView2 = (TextView) convertView.findViewById(R.id.comment_text);
                convertView.setTag(holder);
            } else {
                holder = (IssuesSGAActivity.CustomAdapter.ViewHolder) convertView.getTag();
            }

//            if (position == 0) { position++; }
            Log.d("GetView Position", "" + position);
            holder.textView1.setText(objects.get(position).userName);
            holder.textView2.setText(objects.get(position).mainText);
            return convertView;
        }
    }

    public IssuesSGAActivity.CustomAdapter adapter;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 1)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_sga);
        summary = findViewById(R.id.sga_issue_summary);
        //pref = this.getPreferences(0);
        //editor = pref.edit();
        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        yeaButton = (Button) findViewById(R.id.yay_sga);
        nayButton = (Button) findViewById(R.id.nay_sga);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentsList = (ListView) findViewById(R.id.comment_section_user);
        adapter = new IssuesSGAActivity.CustomAdapter(this, arrayList);
//        adapter_wrapper = new HeaderViewListAdapter(userComList, textComList);
//        adapter1 = new ArrayAdapter<String>(this, R.layout.comment_textview, R.id.commentUsr, userComList);
//        adapter2 = new ArrayAdapter<String>(this, R.layout.comment_textview, R.id.comment_text, userComList);
//        commentsList.setAdapters(adapter1, adapter2);
        commentsList.setAdapter(adapter);
        commentsList.setClickable(false);
        commentsList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(commentsList);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = (yeaNum + nayNum);
                int percentage;
                if (total == 0) {
                    percentage = 0;
                } else {
                    percentage = (yeaNum * 100) / total;
                }
                String str = String.valueOf(percentage) + "%";
                yeaButton.setText(str);
            }
        });

        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = (yeaNum + nayNum);
                int percentage;
                if (total == 0) {
                    percentage = 0;
                } else {
                    percentage = (nayNum * 100) / total;
                }
                String str = String.valueOf(percentage) + "%";
                nayButton.setText(str);
            }
        });

        loadIssuePage();
        loadComments();
    }

    public void loadIssuePage() {
        yeaButton = (Button) findViewById(R.id.yay_sga);
        nayButton = (Button) findViewById(R.id.nay_sga);
        //String idString = pref.getString("idPass", "");
        //String titleString = pref.getString("titlePass", "909090909090");
        //summary.setText(id);
        issues.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                summary.setText(snapshot.child("summary").getValue(String.class));
                yeaNum = snapshot.child("votesYay").getValue(Integer.class);
                nayNum = snapshot.child("votesNay").getValue(Integer.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setTitle(name);
    }

    public void loadComments() {
        //recyclerView = (RecyclerView) findViewById(R.id.comments);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        //fetch();
        //Log.d("loadComments()", "After");

        comments.child(id).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot children : snapshot.getChildren()) {
                    String name = children.child("userName").getValue(String.class);
                    String text = children.child("mainText").getValue(String.class);
                    if (name.equals("")) {
                        continue;
                    }
                    Comment com = new Comment(name, text);
                    arrayList.add(com);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
