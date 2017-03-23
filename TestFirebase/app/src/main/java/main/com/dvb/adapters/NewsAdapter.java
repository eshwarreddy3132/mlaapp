package main.com.dvb.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.com.dvb.R;
import main.com.dvb.homePageFragments.FacebookFragment;
import main.com.dvb.homePageFragments.NewsFragment;
import main.com.dvb.pojos.NewsBean;

/**
 * Created by AIA on 11/16/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{

    private List<NewsBean> newsBeanList = new ArrayList<>();

    public View mView;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView content_news;
        public TextView date_news;
        public TextView subject_news;
        public ImageView feedImg;

        public MyViewHolder(View view) {
            super(view);
            mView = view;

            date_news = (TextView) mView.findViewById(R.id.date_news);
            subject_news = (TextView) mView.findViewById(R.id.subject_news);
            content_news = (TextView) mView.findViewById(R.id.content_news);
            feedImg = (ImageView) mView.findViewById(R.id.feedImg);

        }
    }

    public NewsAdapter(List<NewsBean> newsBeen) {

        this.newsBeanList.addAll(newsBeen);

    }
    public void resetData(List<NewsBean> newsBeanList){
        newsBeanList.clear();
        this.newsBeanList.addAll(newsBeanList);
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(NewsAdapter.MyViewHolder holder, int position) {

        final NewsBean newsBean = newsBeanList.get(position);
        holder.date_news.setText(dateConvertor(newsBean.getDate()));
        holder.subject_news.setText(newsBean.getSubject());
        holder.content_news.setText(newsBean.getContent());
        String imgurl = "https://"+newsBean.getImageURL();
        Glide.with(NewsFragment.newsFragment)
                .load(imgurl)
                .into(holder.feedImg);
        /*if(newsBean.getImageURL() != null && !newsBean.getImageURL().equalsIgnoreCase("")) {
            Log.e("images",""+newsBean.getImageURL());
            holder.feedImg.setVisibility(View.VISIBLE);
            Glide.with(NewsFragment.newsFragment)
                    .load(newsBean.getImageURL())
                    .override(300,300)
                    .into(holder.feedImg);
        }
        else{
            holder.feedImg.setVisibility(View.GONE);
            holder.feedImg.setImageResource(R.mipmap.launchimg);
        }*/
    }


    public String dateConvertor(String dateValue){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateValue);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date == null){
            return "";
        }else{
            return dateToString(date);
        }

    }

    public String dateToString(Date date){
        String datetime="";
        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, ''yy hh:mm a");
        try {
            datetime = myFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetime;
    }


    @Override
    public int getItemCount() {
        return newsBeanList.size();
    }
}
