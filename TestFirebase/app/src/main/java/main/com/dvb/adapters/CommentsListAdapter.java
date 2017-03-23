package main.com.dvb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.com.dvb.R;
import main.com.dvb.pojos.FB.FBCommentBean;

/**
 * Created by AIA on 11/29/16.
 */

public class CommentsListAdapter extends BaseAdapter {

    private ArrayList<FBCommentBean> beanArrayList;
    private static LayoutInflater inflater=null;

    public CommentsListAdapter(ArrayList<FBCommentBean> list, Context context){
        this.beanArrayList = list;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void resetData(ArrayList<FBCommentBean> list){
        this.beanArrayList = list;
    }
    @Override
    public int getCount() {
        return beanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return beanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder viewHolder;

        FBCommentBean fbCommentBean = beanArrayList.get(position);
        if(convertView == null){
            view = inflater.inflate(R.layout.comment_item, null);

            viewHolder = new ViewHolder();
            viewHolder.comment = (TextView) view.findViewById(R.id.comment_message);
            viewHolder.userName=(TextView)view.findViewById(R.id.comment_name);
            viewHolder.commentDate=(TextView)view.findViewById(R.id.comment_date);

            view.setTag( viewHolder );
        }else
            viewHolder=(ViewHolder)view.getTag();

        viewHolder.comment.setText(fbCommentBean.getMessage());
        viewHolder.commentDate.setText(dateConvertor(fbCommentBean.getCreatedTime()));
        viewHolder.userName.setText(fbCommentBean.getUserName());

        return view;
    }

    public static class ViewHolder{

        public TextView comment;
        public TextView userName;
        public TextView commentDate;

    }

    public String dateConvertor(String dateValue){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
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
}
