package in.eoninfotech.eontechnician.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.R;

/**
 * Created by root on 23/5/19.
 */

public class MessageDetails extends AppCompatActivity {

    TextView tv,msgdate,textViewCounter,titles,clientName,cLocation,sender,message;
    FloatingActionButton fab;
    LinearLayout lClient,lLocation;
    String months;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        String date  = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String msgType = intent.getStringExtra("msgType");
        String custName = intent.getStringExtra("custName");
        String locName = intent.getStringExtra("locName");
        String senders = intent.getStringExtra("sender");
        actionBar.setTitle(title);

        tv = findViewById(R.id.message);
        msgdate = findViewById(R.id.date);
        textViewCounter = findViewById(R.id.textViewCounter);
        fab = findViewById(R.id.panic_fab);
        titles = findViewById(R.id.title);
        clientName = findViewById(R.id.clientName);
        cLocation = findViewById(R.id.cLocation);
        sender = findViewById(R.id.sender);
        lClient = findViewById(R.id.lClient);
       // lLocation = findViewById(R.id.lLocation);

        titles.setText(title);
        tv.setText(message);
        clientName.setText(custName);
        cLocation.setText(locName);
        sender.setText(senders);

        if(custName.equalsIgnoreCase("false")&&(locName.equalsIgnoreCase("false"))){
           lClient.setVisibility(View.GONE);
        }else {
            lClient.setVisibility(View.VISIBLE);
           // lLocation.setVisibility(View.VISIBLE);
            clientName.setText(custName);
            cLocation.setText(locName);
        }

        String[] newDate = date.split("/");
        String dates = newDate[0];
        String month = newDate[1];
        String year = newDate[2];

        if (month.equals("01")) {
            months = "Jan";
        } else if (month.equals("02")) {
            months = "Feb";
        } else if (month.equals("03")) {
            months = "Mar";
        } else if (month.equals("04")) {
            months = "Apr";
        } else if (month.equals("05")) {
            months = "May";
        } else if (month.equals("06")) {
            months = "Jun";
        } else if (month.equals("07")) {
            months = "Jul";
        } else if (month.equals("08")) {
            months = "Aug";
        } else if (month.equals("09")) {
            months = "Sep";
        } else if (month.equals("10")) {
            months = "Oct";
        } else if (month.equals("11")) {
            months = "Nov";
        } else if (month.equals("12")) {
            months = "Dec";
        }
        String dateTobeShown = dates + "-" + months + "-" + year;
        msgdate.setText(dateTobeShown);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
