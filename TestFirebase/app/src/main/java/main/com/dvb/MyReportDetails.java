package main.com.dvb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import main.com.dvb.pojos.MyReportsBean;

/**
 * Created by AIA on 11/8/16.
 */

public class MyReportDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_report_details);

        MyReportsBean myReportsBean = (MyReportsBean) getIntent().getSerializableExtra("details");

        TextView name_details = (TextView) findViewById(R.id.name_details);
        TextView date_details = (TextView) findViewById(R.id.date_details);
        TextView mobile_details = (TextView) findViewById(R.id.mobile_details);
        TextView place_details = (TextView) findViewById(R.id.place_details);
        TextView subject_details = (TextView) findViewById(R.id.subject_details);
        TextView complaint_details = (TextView) findViewById(R.id.problem_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name_details.setText(myReportsBean.getName());
        date_details.setText(myReportsBean.getDate());
        mobile_details.setText(myReportsBean.getMobile_number()+"");
        place_details.setText(myReportsBean.getPlace());
        subject_details.setText(myReportsBean.getSubject());
        complaint_details.setText(myReportsBean.getProblem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
