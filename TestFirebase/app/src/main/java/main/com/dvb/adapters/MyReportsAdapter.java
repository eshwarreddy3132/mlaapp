package main.com.dvb.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import main.com.dvb.R;
import main.com.dvb.pojos.MyReportsBean;

/**
 * Created by AIA on 11/6/16.
 */

public class MyReportsAdapter extends BaseAdapter {

    private ArrayList<MyReportsBean> list;
    LayoutInflater inflater;
    public MyReportsAdapter(ArrayList<MyReportsBean> myReportslist, Context context){
        this.list = myReportslist;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.reportitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.subject = (TextView) vi.findViewById(R.id.subject_value);
            holder.date=(TextView)vi.findViewById(R.id.date_value);
            holder.status=(TextView)vi.findViewById(R.id.status_value);
            holder.details = (TextView)vi.findViewById(R.id.details_value);
            holder.testLay = (LinearLayout) vi.findViewById(R.id.testLay);
            holder.card_view = (CardView) vi.findViewById(R.id.card_view);

            holder.date_report=(TextView)vi.findViewById(R.id.date_details);
            holder.name_report=(TextView)vi.findViewById(R.id.name_details);
            holder.subject_report=(TextView)vi.findViewById(R.id.subject_details);
            holder.number_report=(TextView)vi.findViewById(R.id.mobile_details);
            holder.place_report=(TextView)vi.findViewById(R.id.place_details);
            holder.complaint_report=(TextView)vi.findViewById(R.id.problem_details);
            holder.statusImg = (ImageView) vi.findViewById(R.id.statusImg);
            holder.arrow = (ImageView) vi.findViewById(R.id.arrow);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        MyReportsBean myReportsBean = list.get(position);
        holder.subject.setText(myReportsBean.getSubject());
        holder.date.setText(myReportsBean.getDate());
        holder.details.setText(myReportsBean.getProblem());

        holder.date_report.setText(myReportsBean.getDate());
        holder.name_report.setText(myReportsBean.getName());
        holder.subject_report.setText(myReportsBean.getSubject());
        holder.number_report.setText(myReportsBean.getMobile_number()+"");
        holder.place_report.setText(myReportsBean.getPlace());
        holder.complaint_report.setText(myReportsBean.getProblem());

        String status = myReportsBean.getStatus();
        if(status.equalsIgnoreCase("Solved")){
            holder.statusImg.setImageResource(R.mipmap.myreportsolved);
        }else if(status.equalsIgnoreCase("Solving")){
            holder.statusImg.setImageResource(R.mipmap.myreportssolving);
        }else{
            holder.statusImg.setImageResource(R.mipmap.myreportsreceive);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.testLay.getVisibility() == View.GONE) {
                    holder.testLay.setVisibility(View.VISIBLE);
                    holder.arrow.setImageResource(R.mipmap.arrow_up);
                }
                else {
                    holder.testLay.setVisibility(View.GONE);
                    holder.arrow.setImageResource(R.mipmap.arrow_down);
                }
            }
        });

        return vi;
    }

    public static class ViewHolder{

        public TextView subject;
        public TextView date;
        public TextView status;
        public TextView details;
        public LinearLayout testLay;
        public CardView card_view;
        public TextView name_report;
        public TextView date_report;
        public TextView number_report;
        public TextView place_report;
        public TextView subject_report;
        public TextView complaint_report;
        public ImageView statusImg;
        public ImageView arrow;


    }
}
