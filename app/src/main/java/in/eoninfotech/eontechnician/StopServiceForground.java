package in.eoninfotech.eontechnician;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by androidpc on 6/6/19.
 */

public class StopServiceForground extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Intent stopIntent = new Intent(context, ForegroundService.class);
//            stopIntent.setAction(Content.ACTION.STOPFOREGROUND_ACTION);
//            startService(stopIntent);
//        } else {
//            Intent pushIntent = new Intent(context, JobScheduleService.class);
//            context.stopService(pushIntent);
//        }

//        Intent startIntent = new Intent(context, ForegroundService.class);
//        intent.putExtra("param_name", "end");
//        context.stopService(startIntent);
    }
}
