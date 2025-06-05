package in.eoninfotech.eontechnician.helper;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/

public class K {

    public static final String TRY_AGAIN = "Server Response Timeout, Try again!";
    public static final String NO_CONNECTION ="No Connection, Please Try again!";
    public static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue(); }

    public static class Url {
        public static final String BASE_URL = "http://mail.cybernetra.net:8080/android";
        public static final String IMAGE_URL = "http://mis.eon.co.in/eonmis/operations/";
        public static String login_user = BASE_URL+"login_api.php?v=";
        public static String get_stock_detail = BASE_URL+"client_data.php?v=";
        public static String gettechnician = BASE_URL+"tech_list.php?v=";
        public static String get_techniciandetail = BASE_URL+"distance_api.php?v=";
        public static String getclients = BASE_URL+"client_list.php?v=";
        public static String set_new_installment = BASE_URL+"device_new.php";
        public static String set_stock_detail = BASE_URL+"client_update.php";
        public static String get_check_list = BASE_URL+"work_list.php?v=";
        public static String getapk = "http://mail.cybernetra.net:8080/android/tech/fieldstatus.apk";
        public static String urlkey = "eon@180#$@1";
        public static int chkversion = 23; }


    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(true);
        return dialog;
    }

    public static String  getDateFormatWithMonthNameHyphen(String str){
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd-MMM-yyyy");
        String date2 = spf.format(newDate);

        return date2;
    }

    public static String  getDateFormatWithMonthNameHyphenYear(String str){
        SimpleDateFormat spf=new SimpleDateFormat("dd-MM-yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("yyyy-MM-dd");
        String date2 = spf.format(newDate);

        return date2;
    }
}
