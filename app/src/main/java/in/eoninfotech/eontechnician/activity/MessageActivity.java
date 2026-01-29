package in.eoninfotech.eontechnician.activity;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import in.eoninfotech.eontechnician.AppPreferences;
import in.eoninfotech.eontechnician.R;

import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.MessageDetail;
import in.eoninfotech.eontechnician.webservice.MessageResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity  implements MessageAdapter.MessageAdapterListener, View.OnClickListener, TextWatcher {

    View v;
    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public LinearLayoutManager layoutManager;
    public LinearLayout linearActivity;
    private TextView txtContentUnavailable;
    String current_date, selected_todate, s_date,username,version;
    ArrayList<MessageDetail> activityDetail = new ArrayList<>();
    MessageAdapter messageAdapter;
    private SearchView searchView;
    TextView textCartItemCount;
    CardView search_card;
    ShimmerFrameLayout mShimmerViewContainer;
    int rateCounter=0;
    Toolbar toolbar;
    boolean hasText=false;
    int year, month, day, hour, minutes;
    AppPreferences appPrefs;
    Calendar calen = Calendar.getInstance();
    MessageAdapter.MessageAdapterListener listener;
    String status = "0";
    String msg_type = "";
    String message_id = "";
    String itemPressed = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Messages");
        appPrefs = new AppPreferences(getApplicationContext());
        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("s_uuser", "");
        version = sharedprefs.getString("version", "");
        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        txtContentUnavailable = findViewById(R.id.txt_content_unavailable);
        recyclerView.setLayoutManager(layoutManager);
        linearActivity = findViewById(R.id.llContent);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);
        refreshLayout.setOnRefreshListener(this::refresh);
        refreshLayout.setRefreshing(true);
        listener  = new MessageAdapter.MessageAdapterListener() {
            @Override
            public void onMessageRowClicked(int position) {
                MessageDetail message = activityDetail.get(position);
                message_id  = activityDetail.get(position).getId();
                Intent intent = new Intent(MessageActivity.this,MessageDetails.class);
                intent.putExtra("message",activityDetail.get(position).getMessage());
                intent.putExtra("date",activityDetail.get(position).getDatetime());
                intent.putExtra("title",activityDetail.get(position).getTitle());
                intent.putExtra("msgType",activityDetail.get(position).getMsg_type());
                intent.putExtra("custName",activityDetail.get(position).getCust_name());
                intent.putExtra("locName",activityDetail.get(position).getLoc_name());
                intent.putExtra("sender",activityDetail.get(position).getSender());
                startActivity(intent);
                updateData();
            }
        };
        setDateAndTime();
        loadContent();
    }
    private void updateData() {
       // refreshLayout.setRefreshing(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MainResponse> call = log_att.updateResponse(message_id);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if(response.body().getType()==1) {
                    loadContent();
                }else{
                }
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
              //  refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                messageAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                try {
                    messageAdapter.getFilter().filter(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        final MenuItem menuItem = menu.findItem(R.id.menu_filter);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        textCartItemCount.setVisibility(View.GONE);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }
    private void setupBadge() {
      if(itemPressed.equals("3")&&(textCartItemCount.getVisibility()==View.VISIBLE)){
          textCartItemCount.setVisibility(View.GONE);
        }else{
          textCartItemCount.setVisibility(View.GONE);
      }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_filter:
                View menuItemView = findViewById(R.id.menu_filter); // SAME ID AS MENU ID
                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                popupMenu.inflate(R.menu.verify);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.filter_date) {
                            status = "0";
                            msg_type = "";
                            getDate();
                            textCartItemCount.setVisibility(View.VISIBLE);
                        }else if(id == R.id.verifyRead){
                            s_date="";
                            status="1";
                            msg_type = "";
                            loadContent();
                            textCartItemCount.setVisibility(View.VISIBLE);
                        }else if(id == R.id.verifyUnread){
                            s_date="";
                            status = "2";
                            msg_type = "";
                            loadContent();
                            textCartItemCount.setVisibility(View.VISIBLE);
                        }else if(id == R.id.filterFaulty){
                            s_date="";
                            status = "0";
                            msg_type = "F";
                            loadContent();
                            textCartItemCount.setVisibility(View.VISIBLE);
                        } else if(id == R.id.clearFilter){
                            s_date="";
                            status = "0";
                            itemPressed= "3";
                            msg_type = "";
                            loadContent();
                            setupBadge();
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            default:
                search_card.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    private void setDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        year = calen.get(Calendar.YEAR);
        month = calen.get(Calendar.MONTH);
        day = calen.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        hour = calen.get(Calendar.HOUR_OF_DAY);
        minutes = calen.get(Calendar.MINUTE);
        if (month + 1 < 10) {
            current_date = day + "-0" + month + "-" + year;
        } else {
            current_date = day + "-" + month + "-" + year;
        }
        String abc = current_date;
        String[] separated = abc.split("-");
        String date =  separated[0];
        String month = separated[1];
        String years = separated[2];
        String dates  = years+"-"+month+"-"+date;
    }

    private void getDate() {
      //  Log.i("***string**", t_install_date.getText().toString());
        // TODO Auto-generated method stub
        final DatePickerDialog dpdd = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            if (monthOfYear + 1 < 10) {
                selected_todate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
            } else {
                selected_todate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            }
            String abc = selected_todate;
            String[] separated = abc.split("-");
            String date =  separated[0];
            String month = separated[1];
            String years = separated[2];
            String dates  = years+"-"+month+"-"+date;
            s_date = dates;
            refreshLayout.setRefreshing(true);
            loadContent();
        }, year, month - 1, day);
        dpdd.getDatePicker().setMaxDate(calen.getTimeInMillis());
        dpdd.show();
    }

    private void loadContent() {
       // refreshLayout.setRefreshing(true);
        ApiHolder log_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
        Call<MessageResponse> call = log_att.messageResponse(username,s_date,status,msg_type);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.body().getType()==1) {
                    MessageResponse activityResponse = response.body();
                    txtContentUnavailable.setVisibility(View.GONE);
                    activityDetail = activityResponse.getMsg_list();
                    messageAdapter = new MessageAdapter(getApplicationContext(), activityDetail,listener);
                    recyclerView.setAdapter(messageAdapter);
                    runLayoutAnimation(recyclerView);
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    //mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    //ShowProgressBar(false);
                } else {
                    txtContentUnavailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    //mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
              //  refreshLayout.setRefreshing(false);
            }
        });
    }

    private void runLayoutAnimation(RecyclerView recyclerView) {

        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    private void refresh() {
        s_date = "";
        loadContent();
        setupBadge();
    }
    @Override
    public void onMessageRowClicked(int position) {
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void afterTextChanged(Editable editable) {
    }
    @Override
    public void onClick(View view) {
    }
    private void CallRateCounter() {
        rateCounter++;
        appPrefs.setRateClickCounter(rateCounter);
        Log.d("RateCounter",""+appPrefs.getRateClickCounter());
    }

}
