package in.eoninfotech.eontechnician;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import in.eoninfotech.eontechnician.Service.ForegroundService;
import in.eoninfotech.eontechnician.Service.JobScheduleService;
import in.eoninfotech.eontechnician.Service.LocationService;

import static com.thefinestartist.utils.content.ContextUtil.getApplicationContext;
import static com.thefinestartist.utils.content.ContextUtil.getSystemService;

/**
 * Created by root on 26/4/19.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Intent pushIntent = new Intent(context, ForegroundService.class);
                pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(pushIntent);
//            } else {
//                Intent pushIntent = new Intent(context, JobScheduleService.class);
//                pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startService(pushIntent);
//            }
//
        }
    }
}
