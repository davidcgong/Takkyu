package com.davidcgong.takkyu;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //names and images for recycler view
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageURLs = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private android.support.v7.widget.Toolbar mToolbar;
    private TextView testText;
    private RecyclerViewAdapter adapter;

    private String loadedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 100; i++) {
            loadedText = "hey there this is not doing anything";

            mImageURLs.add("https://www.japan-guide.com/thumb/destination_tokyo.jpg");
            mNames.add(loadedText);
            Log.d(TAG, "onCreate: loadedText val: " + loadedText);
            if (i == 99) {
                Log.d(TAG, "onCreate: testingIndexVal: " + mNames.get(50));
            }
        }


        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mToolbar = findViewById(R.id.nav_action);
       // testText = findViewById(R.id.textView);
        setSupportActionBar(mToolbar);

       new jsoupTest().execute();


        //init recycler view
        RecyclerView recyclerView = findViewById(R.id.home_list);
        adapter = new RecyclerViewAdapter(this, mNames, mImageURLs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        new jsoupTest().execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //see back navigation button

    }

//    private initImageBitmaps() {
//
//    }

    public class jsoupTest extends AsyncTask<Void, Void, Void> {
        private String loadedText;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("https://www.ittf.com/news/").get();
                loadedText = doc.getElementsByClass("media media-post media-post-featured").html();
                loadedText = doc.getElementsByTag("a").text();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            // testText.setText(loadedText);
            mNames.set(0, loadedText);
            Log.d(TAG, "onPostExecute: indexAt0 is: " + mNames.get(0));
//            mImageURLs.add("https://www.japan-guide.com/thumb/destination_tokyo.jpg");
            //            mNames.add(loadedText);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
