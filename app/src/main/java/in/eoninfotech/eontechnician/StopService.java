package in.eoninfotech.eontechnician;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import in.eoninfotech.eontechnician.service.ForegroundService;


/**
 * Created by root on 30/4/19.
 */

public class StopService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            Intent startIntent = new Intent(context, ForegroundService.class);
            intent.putExtra("param_name", "end");
            context.stopService(startIntent);
        }
}
