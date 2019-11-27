package in.eoninfotech.eontechnician.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import in.eoninfotech.eontechnician.R;


/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/
public class CheckConnection {

	Context context;

	public CheckConnection(Context context) {
		this.context = context;
	}

	public boolean isConnected() {
		ConnectivityManager connectivityManager;
		NetworkInfo wifiInfo, mobileInfo;
		connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		mobileInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifiInfo.isConnected() || mobileInfo.isConnected())
			return true;
		else
			return false;
	}

	public boolean isGpsOn() {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	public void showGPSErrorDialog() {
		new AlertDialog.Builder(context)
				.setTitle("Enable GPS!")
				.setMessage(
						"There is problem fetching your location. Please turn on your gps.")
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent viewIntent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								context.startActivity(viewIntent);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// ((Activity) context).finish();
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show()
				.setCancelable(false);
	}

	public void showConnectionErrorDialog() {
		new AlertDialog.Builder(context)
				.setTitle("No Connection!")
				.setMessage(
						"\n Please enable your connection. \n")
				.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								context.startActivity(new Intent(
										Settings.ACTION_SETTINGS));
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// ((Activity) context).finish();
								dialog.cancel();

							}
						}).setIcon(R.drawable.ic_warning_black_24dp).show()
				.setCancelable(false);
	}
}
