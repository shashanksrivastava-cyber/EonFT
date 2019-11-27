package in.eoninfotech.eontechnician;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.thefinestartist.Base;

import java.util.Calendar;

import in.eoninfotech.eontechnician.Service.ForegroundService;
import in.eoninfotech.eontechnician.Service.JobScheduleService;
import in.eoninfotech.eontechnician.Service.LocationService;
import in.eoninfotech.eontechnician.activity.SimpleServiceExample;
import in.eoninfotech.eontechnician.fragments.DashBoardFragment;

import static com.thefinestartist.utils.content.ContextUtil.startService;

/**
 * Created by root on 17/5/19.
 */

public class AlarmReceiver extends BroadcastReceiver {

    int MID ;
    @Override
    public void onReceive(Context context, Intent intent) {

        Base.initialize(context);

    }
}
