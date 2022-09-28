package in.eoninfotech.eontechnician;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.aakira.expandablelayout.Utils;


import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FaqDetailActivity extends AppCompatActivity {

    SharedPreferences sharedprefs;
    SharedPreferences.Editor editor;
    String username, dist_id,version;
    public RecyclerView recyclerView;
    public SwipeRefreshLayout refreshLayout;
    public GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqs_report);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE);
        editor = sharedprefs.edit();
        username = sharedprefs.getString("usname", "");
        dist_id = sharedprefs.getString("s_distt", "");
        version = sharedprefs.getString("version", "");

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecorator(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<ItemModel> data = new ArrayList<>();
        data.add(new ItemModel(
                "Power Cable Disconnected from Vehicle's Battery",
                R.color.dash_red,
                R.color.dash_red,
                Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR)));
        data.add(new ItemModel(
                "Earth Wire Removed",
                R.color.dark_greys,
                R.color.dark_greys,
                Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR)));
        data.add(new ItemModel(
                "Power Cable Damage/Cut",
                R.color.dash_red,
                R.color.dash_red,
                Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR)));
        data.add(new ItemModel(
                "Main Power Supply cut off from Main Switch",
                R.color.brown,
                R.color.brown,
                Utils.createInterpolator(Utils.DECELERATE_INTERPOLATOR)));
        recyclerView.setAdapter(new RecyclerViewRecyclerAdapter(data));

    }
}
