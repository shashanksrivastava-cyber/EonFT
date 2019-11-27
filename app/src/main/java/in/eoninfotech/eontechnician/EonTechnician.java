package in.eoninfotech.eontechnician;

import android.app.Application;
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/

/**
 * Created by root on 12/9/16.
 */
public class EonTechnician extends Application {

    private static int pendingNotificationsCount = 0;
    private static String Tm_name ;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static int getPendingNotificationsCount() {
        return pendingNotificationsCount;
    }

    public static void setPendingNotificationsCount(int pendingNotifications) {
        pendingNotificationsCount = pendingNotifications;
    }
    public static String getTm_name() {
        return Tm_name;
    }

    public static void setTm_name(String Tm_nam) {
        Tm_name= Tm_nam;
    }
}
