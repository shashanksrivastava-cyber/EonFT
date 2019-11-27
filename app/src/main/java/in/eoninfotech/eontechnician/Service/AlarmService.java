package in.eoninfotech.eontechnician.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.thefinestartist.Base;

import java.util.Calendar;

import in.eoninfotech.eontechnician.Content;
import in.eoninfotech.eontechnician.activity.SimpleServiceExample;

import static com.thefinestartist.utils.content.ContextUtil.startService;

/**
 * Created by root on 21/5/19.
 */

public class AlarmService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Base.initialize(context);

        Intent startIntent = new Intent(context, ForegroundService.class);
        startIntent.setAction(Content.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);

    }
}
