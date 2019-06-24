package com.anlyze.newsboard.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anlyze.newsboard.R;
import com.anlyze.newsboard.models.NewsData;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{

    //vars
    private ArrayList<NewsData> newsDataArrayList = new ArrayList<>();
    private Context mContext;
    private Typeface textFont;
    private NewsRecyclerClickListener newsRecyclerClickListener;

    public NewsListAdapter(ArrayList<NewsData> newsDataArrayList, Context mContext, NewsRecyclerClickListener mNewsRecyclerClickListener) {
        this.newsDataArrayList = newsDataArrayList;
        this.mContext = mContext;
        this.newsRecyclerClickListener = mNewsRecyclerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_cards, parent, false);
        final ViewHolder holder = new ViewHolder(view,newsRecyclerClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NewsData newsData = newsDataArrayList.get(position);

        ((ViewHolder)holder).title.setText(newsData.getTitle());
        ((ViewHolder)holder).news_text.setText(newsData.getDescription());
        if( newsData.getSource() == null || newsData.getSource().equals("null") ){
            ((ViewHolder)holder).source.setText("Google News");
        }else{
            ((ViewHolder)holder).source.setText(newsData.getSource());
        }

        try {
            Date date1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(newsData.getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try{
            Glide.with(mContext).load(newsData.getUrlToImage())
                    .thumbnail(0.5f)
                    .into(((ViewHolder)holder).newsImg);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return newsDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //Widgets
        TextView title,news_text,source,time;
        ImageView newsImg;
        NewsRecyclerClickListener clickListener;

        public ViewHolder(View itemView, NewsRecyclerClickListener moduleRecyclerClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            news_text = itemView.findViewById(R.id.news_text);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            newsImg = itemView.findViewById(R.id.news_img);

            textFont = Typeface.createFromAsset( mContext.getAssets(),"fonts/montserrat_semibold.ttf");
            title.setTypeface(textFont);
            news_text.setTypeface(textFont);
            source.setTypeface(textFont);
            time.setTypeface(textFont);
            this.clickListener = moduleRecyclerClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onNewsSelected(getAdapterPosition());
        }
    }

    public interface NewsRecyclerClickListener {
        public void onNewsSelected(int position);
    }
}
