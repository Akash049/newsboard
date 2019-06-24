package com.anlyze.newsboard.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anlyze.newsboard.R;
import com.anlyze.newsboard.adapters.NewsListAdapter;
import com.anlyze.newsboard.models.NewsData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.anlyze.newsboard.Utility.UtilityFunction.isNetworkConnected;
import static com.anlyze.newsboard.config.config.NEWS_URL;

public class NewsBoard extends AppCompatActivity implements View.OnClickListener, NewsListAdapter.NewsRecyclerClickListener {

    //Recycler Viewer setup
    private RecyclerView newsRecyclerView;
    private NewsListAdapter newsRecyclerAdapter;
    private ArrayList<NewsData> newsDataArrayList =  new ArrayList<NewsData>();
    private NewsData newsData;

    //widget
    private Typeface typeface;
    private TextView header_text;
    private ImageView search, check;
    private EditText searchTitle;
    private Button refresh;
    private LinearLayout connectErrorBlock, progressBlock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsboard);
        initialize();
        //initiate();
        if( isNetworkConnected(NewsBoard.this) ){
            newsRecyclerView.setVisibility(View.GONE);
            progressBlock.setVisibility(View.VISIBLE);
            connectErrorBlock.setVisibility(View.GONE);
            fetchNewsData();
        }else{
            newsRecyclerView.setVisibility(View.GONE);
            progressBlock.setVisibility(View.GONE);
            connectErrorBlock.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Internet not connected",Toast.LENGTH_LONG).show();
        }
    }

    public void initialize(){

        search = (ImageView)findViewById(R.id.search);
        search.setOnClickListener(this);
        searchTitle = (EditText)findViewById(R.id.search_title);
        check = (ImageView)findViewById(R.id.correct);
        check.setOnClickListener(this);

        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        header_text = (TextView)findViewById(R.id.header_text);
        typeface = Typeface.createFromAsset(getAssets(),"fonts/quicksand_regular.ttf");
        header_text.setTypeface(typeface);

        newsRecyclerView = (RecyclerView)findViewById(R.id.analysis_recycler_view);
        newsRecyclerAdapter =  new NewsListAdapter(newsDataArrayList,getApplicationContext(),this);
        newsRecyclerView.setAdapter(newsRecyclerAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressBlock = (LinearLayout)findViewById(R.id.progress_block);
        connectErrorBlock = (LinearLayout)findViewById(R.id.connection_block);
    }

    public void initiate(){
        newsData = new NewsData();
        newsDataArrayList.add(newsData);

        newsData = new NewsData();
        newsDataArrayList.add(newsData);

        newsData = new NewsData();
        newsDataArrayList.add(newsData);

        newsData = new NewsData();
        newsDataArrayList.add(newsData);

        newsData = new NewsData();
        newsDataArrayList.add(newsData);

        newsData = new NewsData();
        newsDataArrayList.add(newsData);

        newsRecyclerAdapter.notifyDataSetChanged();
    }

    private void fetchNewsData() {
        try{
            RequestQueue queue = Volley.newRequestQueue(NewsBoard.this);
            StringRequest postRequest = new StringRequest(Request.Method.GET, NEWS_URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try{
                                JSONObject responseObject = new JSONObject(response);
                                String status = responseObject.optString("status");
                                if( status.equals("ok") ){

                                    JSONObject dataObj;

                                    JSONArray resultArray = responseObject.getJSONArray("articles");


                                    for( int i = 0; i < resultArray.length() ; i++ ){

                                        dataObj = resultArray.getJSONObject(i);

                                        newsData = new NewsData();

                                        newsData.setTitle(dataObj.optString("title"));
                                        newsData.setAuthor(dataObj.optString("author"));
                                        newsData.setSource(dataObj.optString("author"));
                                        newsData.setDescription(dataObj.optString("description"));
                                        newsData.setPublishedAt(dataObj.optString("publishedAt"));
                                        newsData.setUrl(dataObj.optString("url"));
                                        newsData.setUrlToImage(dataObj.optString("urlToImage"));

                                        newsDataArrayList.add(newsData);
                                    }
                                    progressBlock.setVisibility(View.GONE);
                                    connectErrorBlock.setVisibility(View.GONE);
                                    newsRecyclerView.setVisibility(View.VISIBLE);

                                }else{
                                    progressBlock.setVisibility(View.GONE);
                                    connectErrorBlock.setVisibility(View.VISIBLE);
                                    newsRecyclerView.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Connection error", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){

                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // As of f605da3 the following should work
                            NetworkResponse response = error.networkResponse;

                            progressBlock.setVisibility(View.GONE);
                            connectErrorBlock.setVisibility(View.VISIBLE);
                            newsRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Connection error", Toast.LENGTH_SHORT).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> loginParams = new HashMap<String, String>();
                    return loginParams;
                }
            };
            queue.add(postRequest);

        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.refresh:
                if( isNetworkConnected(NewsBoard.this) ){
                    newsRecyclerView.setVisibility(View.GONE);
                    progressBlock.setVisibility(View.VISIBLE);
                    connectErrorBlock.setVisibility(View.GONE);
                    fetchNewsData();
                }else{
                    newsRecyclerView.setVisibility(View.GONE);
                    progressBlock.setVisibility(View.GONE);
                    connectErrorBlock.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Internet not connected",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.search:
                header_text.setVisibility(View.GONE);
                searchTitle.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                check.setVisibility(View.VISIBLE);
                break;
            case R.id.correct:
                header_text.setVisibility(View.VISIBLE);
                searchTitle.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                check.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNewsSelected(int position) {

    }
}
