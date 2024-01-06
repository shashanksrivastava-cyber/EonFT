//*********************************************************************************************
//Copyright EON Infotech Ltd., unpublished work, created Aug 2012.
//This computer program includes Confidential, Proprietary Information 
//and is
//a Trade Secret of EON Infotech Ltd. All use, disclosure, and/or 
//reproduction
//is prohibited unless authorised in writing by an authorised officer of EON Infotech Ltd.
//All Rights Reserved.
//********************************************************************************************
//SITE           : EON Infotech Ltd., C-180, Phase 8B, Ind. Area, Mohali
//PROJECT        : CTU
//TITLE          : CTU
//FILE           : EONUtil.java
//Original Author:
//Created Date   : Oct,2016
//Revision       :
//
//Revision History
//+-------+--------+-------+----------------------------+-----------+----------+
//|  SNo. |  Date  |   By  |    Changes made/Ticket No. |  Remarks  |New RevNo.|
//+-------+--------+-------+----------------------------+-----------+----------+
//
//
//*********************************************************************************************
//
//DESCRIPTION    :  This code is used to create the database for Punbus.
//
//*********************************************************************************************

package in.eoninfotech.eontechnician.helper;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.eoninfotech.eontechnician.myprovider.EonContentProvider.*;


public class EONUtil {

	private static EONUtil myutils = null;

	/**
	 * This method used to get the instance of utils class.
	 * 
	 * @return instance of utils class.
	 */
	public static EONUtil getInstance() {

		if (myutils == null) {
			myutils = new EONUtil();
		}
		return myutils;
	}

	/**
	 * This method is used to inserting the information of station i.e. id and
	 * station name into the database.
	 * 
	 * @param context
	 * @param id
	 * @param stationid
	 * @param stationname
	 */
	public static void insertStationData(Context context, int id,
			String stationid, String stationname) {

		try {

			ContentResolver resolver = context.getContentResolver();
			ContentValues contentValues = new ContentValues();

			contentValues.put(CtuColumn._ID, id);
			contentValues.put(CtuColumn.PUNBUS_STATION_ID, stationid);
			contentValues.put(CtuColumn.PUNBUS_STATION_NAME, stationname);
			Uri uri = resolver.insert(CtuColumn.CONTENT_URI, contentValues);
			Log.i("****insert***", String.valueOf(uri));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void deleteStationData(Context context) {
		try {

			ContentResolver resolver = context.getContentResolver();

			int uri = resolver.delete(CtuColumn.CONTENT_URI,null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to get the station detail i.e. station id and station
	 * name of station from the database in the form of cursor .
	 * 
	 * @param context
	 * @return cursor
	 */
	public static Cursor getStationDetail(Context context) {
		Log.e("", "calling the getDetailCusror");

		String[] stationprojection = new String[3];
		stationprojection[0] = CtuColumn._ID;
		stationprojection[1] = CtuColumn.PUNBUS_STATION_ID;
		stationprojection[2] = CtuColumn.PUNBUS_STATION_NAME;
		return context.getContentResolver().query(CtuColumn.CONTENT_URI,
				stationprojection, null, null, null
		// CtuColumn.ID + " asc "
				); // asc

	}

	private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";

	/**
	 * This method is used to get the current date
	 * 
	 * @return date
	 */
	public static String getCurrentDate() {

		return new SimpleDateFormat(DATE_TIME_FORMAT).format(Calendar
				.getInstance().getTime());

	}

	/**
	 * This method is used to get the station detail i.e. station id and station
	 * name of station from the database in the form of cursor .
	 * 
	 * @param context
	 * @return hashmap
	 */

	public static HashMap<String, String> gettingData(Context context) {
		String stationname;
		String stationid;
		ArrayList<String> stationList = new ArrayList<String>();
		HashMap<String, String> stationmap = new HashMap<String, String>();
		Cursor cursor = getStationDetail(context);
		if (cursor.moveToFirst()) {
			do {
				try {
					stationname = cursor.getString(cursor
							.getColumnIndex(CtuColumn.PUNBUS_STATION_NAME));
					stationid = cursor.getString(cursor
							.getColumnIndex(CtuColumn.PUNBUS_STATION_ID));
					stationmap.put(stationid, stationname);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}

		return stationmap;

	}
	public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
