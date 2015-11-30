package com.barantech.sayaword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.barantech.sayaword.util.Log;
import com.barantech.sayaword.web.model.SubscribersResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SubscribersActivity extends AppCompatActivity {
    public static final String EXTRA_SUBSCRIBERS = "extra_subscribers";

    TextView mTvTitle;
    Gson gson = new Gson();
    ArrayList<SubscribersResponse.Subscriber> subscribers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribers);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        if(getIntent().hasExtra(EXTRA_SUBSCRIBERS)){
            String extra = getIntent().getExtras().getString(EXTRA_SUBSCRIBERS);
            Log.d(extra);
            SubscribersResponse response = gson.fromJson(extra, SubscribersResponse.class);
            subscribers = response.subscribers;
        }
        rv.setAdapter(new SubscribersAdapter(subscribers));
    }
}
