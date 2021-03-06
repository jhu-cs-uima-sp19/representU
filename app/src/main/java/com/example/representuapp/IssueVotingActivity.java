package com.example.representuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class IssueVotingActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference issues = db.getReference().child("issues");
    private DatabaseReference comments = db.getReference().child("comments");
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HeaderViewListAdapter adapter_wrapper;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    public Integer yeaNum = 0;
    public Integer nayNum = 0;
    public Button nayButton;
    public Button yeaButton;
    //public List<Comment> commentsList;
    public List<String> votedYayList;
    public List<String> votedNayList;
    public ArrayList<String> textComList;
    public ArrayList<String> userComList;
    public TextView summary;
    public ListView commentsList;
    public String title;
    public String idString;
    public boolean hasVotedYay;
    public boolean hasVotedNay;
    String name;
    String id;
    int colorPrimaryDark;
    int colorAccentDark;
    int white;
    String JHED;
    ArrayList<Comment> arrayList = new ArrayList<>();
    TextView comUser;
    TextView comText;
    private SharedPreferences myPrefs;
    Layout comment_layout;

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
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.comment_textview, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.commentUsr);
                holder.textView2 = (TextView) convertView.findViewById(R.id.comment_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            if (position == 0) { position++; }
            Log.d("GetView Position", "" + position);
            holder.textView1.setText(objects.get(position).userName);
            holder.textView2.setText(objects.get(position).mainText);
            return convertView;
        }
    }

    public CustomAdapter adapter;

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
        setContentView(R.layout.activity_issue_voting);
        summary = findViewById(R.id.user_issue_summary);
        comUser = findViewById(R.id.commentUsr);
        comText = findViewById(R.id.comment_text);

//        = new ListView.FixedViewInfo(comUser);

        //comment_layout = R.layout.comment_textview;


        Intent intent = getIntent();
        JHED = intent.getStringExtra("JHED");



        commentsList = (ListView) findViewById(R.id.comment_section_user);
        adapter = new CustomAdapter(this, arrayList);
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

        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        JHED = myPrefs.getString("JHED", "user");

        name = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        yeaNum = intent.getIntExtra("yea", 0);
        nayNum = intent.getIntExtra("nay", 0);
        yeaButton = findViewById(R.id.yay_user);
        nayButton = findViewById(R.id.nay_user);

        //initialize colors
        colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        colorAccentDark = ContextCompat.getColor(this, R.color.colorAccentDark);
        white = ContextCompat.getColor(this, R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasVotedYay) {
                    invalidateOptionsMenu();
                    yeaNum--;
                    issues.child(id).child("votesYay").setValue(yeaNum);
                    votedYayList.remove(JHED);
                    issues.child(id).child("usersYay").setValue(votedYayList);
                    Toast.makeText(IssueVotingActivity.this, "Vote Revoked", Toast.LENGTH_SHORT).show();
                } else {
                    invalidateOptionsMenu();
                    yeaNum++;
                    issues.child(id).child("votesYay").setValue(yeaNum);
                    votedYayList.add(JHED);
                    issues.child(id).child("usersYay").setValue(votedYayList);
                    Toast.makeText(IssueVotingActivity.this, "You voted Yea!", Toast.LENGTH_SHORT).show();

                    // Starts stats activity and sends title, id, votes for/against, and how the user voted
                    Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("id", id);
                    intent.putExtra("yea", yeaNum);
                    intent.putExtra("nay", nayNum);
                    intent.putExtra("voted", "Yea");
                    startActivity(intent);
                }
            }
        });


        nayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasVotedNay) {
                    invalidateOptionsMenu();
                    nayNum--;
                    issues.child(id).child("votesNay").setValue(nayNum);
                    votedNayList.remove(JHED);
                    issues.child(id).child("usersNay").setValue(votedNayList);
                    Toast.makeText(IssueVotingActivity.this, "Vote Revoked", Toast.LENGTH_SHORT).show();
                } else {
                    invalidateOptionsMenu();
                    nayNum++;
                    issues.child(id).child("votesNay").setValue(nayNum);
                    votedNayList.add(JHED);
                    issues.child(id).child("usersNay").setValue(votedNayList);
                    Toast.makeText(IssueVotingActivity.this, "You voted Nay!", Toast.LENGTH_SHORT).show();

                    // Starts stats activity and sends title, id, votes for/against, and how the user voted
                    Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("id", id);
                    intent.putExtra("yea", yeaNum);
                    intent.putExtra("nay", nayNum);
                    intent.putExtra("voted", "Nay");

                    startActivity(intent);
                }
            }
        });

        loadIssuePage();
        loadComments();
        addComment();

    }

    @Override
    public void onResume(){
        super.onResume();
        loadComments();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
        adapter.notifyDataSetChanged();
    }

    public void loadIssuePage() {

        issues.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                summary.setText(snapshot.child("summary").getValue(String.class));
                yeaNum = snapshot.child("votesYay").getValue(Integer.class);
                nayNum = snapshot.child("votesNay").getValue(Integer.class);
                GenericTypeIndicator<ArrayList<String>> gti =new GenericTypeIndicator<ArrayList<String>>(){};
                votedYayList = snapshot.child("usersYay").getValue(gti);
                votedNayList = snapshot.child("usersNay").getValue(gti);
                if (votedNayList == null) {
                    votedNayList = new ArrayList<>();
                    votedYayList.add(" ");
                    issues.child(id).child("usersNay").setValue(votedNayList);

                }
                if (votedYayList == null) {
                    votedYayList = new ArrayList<>();
                    votedYayList.add(" ");
                    issues.child(id).child("usersYay").setValue(votedYayList);
                }
                hasVotedNay = checkVotedNay(JHED);
                hasVotedYay = checkVotedYay(JHED);

                //Check that visibility makes sense.
                if (hasVotedYay) {
                    nayButton.setVisibility(View.GONE);
                    yeaButton.setVisibility(View.VISIBLE);
                } else if(hasVotedNay) {
                    nayButton.setVisibility(View.VISIBLE);
                    yeaButton.setVisibility(View.GONE);
                } else {
                    nayButton.setVisibility(View.VISIBLE);
                    yeaButton.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        setTitle(name);
    }

    public void addComment() {
        Button addCom = findViewById(R.id.add_comment_button);
        addCom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(IssueVotingActivity.this, AddCommentActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("JHED", JHED);
                startActivity(intent);
            }
        });
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

    public boolean checkVotedYay(String userName) {
        if (votedYayList.contains(userName)) {
            nayButton.setVisibility(View.GONE);
            yeaButton.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public boolean checkVotedNay(String userName) {
        if (votedNayList.contains(userName)) {
            nayButton.setVisibility(View.VISIBLE);
            yeaButton.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (checkVotedNay(JHED) || checkVotedYay(JHED)) {
            getMenuInflater().inflate(R.menu.user_voting, menu);
            MenuItem stats = menu.findItem(R.id.action_stats);
            stats.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_stats) {
            if (checkVotedNay(JHED)) {
                Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                intent.putExtra("title", name);
                intent.putExtra("id", id);
                intent.putExtra("yea", yeaNum);
                intent.putExtra("nay", nayNum);
                intent.putExtra("voted", "Nay");
                startActivity(intent);
            } else if (checkVotedYay(JHED)) {
                Intent intent = new Intent(IssueVotingActivity.this, UserStatisticsActivity.class);
                intent.putExtra("title", name);
                intent.putExtra("id", id);
                intent.putExtra("yea", yeaNum);
                intent.putExtra("nay", nayNum);
                intent.putExtra("voted", "Yea");
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
