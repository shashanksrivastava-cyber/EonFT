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
//FILE           : AppPreferences.java
//Original Author: Harman tj
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
//DESCRIPTION    :   This code is used to store the data. 
//*********************************************************************************************


package in.eoninfotech.eontechnician;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class AppPreferences {
	private static final String APP_SHARED_PREFS = "in.eoninfotech.eontechnician.preference";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;
	private static final String LOGGED_IN = "logged_in";
	private static final String IS_FIRST_LAUNCH = "is_first_launch";
	private static final String USER_TYPE = "userType";
	private static final String RATE_CLICK_COUNTER = "rate_click_counter";
	private static final String IS_READ = "is_read";


	/**
	 * @param context
	 */
	public AppPreferences(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	/**
	 * @return providerinfo
	 */
	public String getproviderInfo() {
		return appSharedPrefs.getString("provider", "");
	}

	/**
	 * @return addsourcestation
	 */

	public String getAddSourceStationInfo() {
		return appSharedPrefs.getString("addsourcestation", "");
	}

	/**
	 * @return removeSourceStationInfo
	 */

	public String getRemoveSourceStationInfo() {
		return appSharedPrefs.getString("removesourcestation", "");
	}

	/**
	 * @return destinationstation
	 */

	public String getAddDestinationStationInfo() {
		return appSharedPrefs.getString("adddestinationstation", "");
	}

	/**
	 * @return removedestination info.
	 */
	public String getRemoveDestinationStationInfo() {
		return appSharedPrefs.getString("removedestinationstation", "");
	}

	/**
	 * @param data
	 */
	public void saveAddSourceStationInfo(String data) {
		prefsEditor.putString("addsourcestation", data);
		prefsEditor.commit();
	}

	/**
	 * @param data
	 */
	public void saveRemoveSourceStationInfo(String data) {
		prefsEditor.putString("removesourcestation", data);
		prefsEditor.commit();
	}

	/**
	 * @param data
	 */
	public void saveAddDestinationStationInfo(String data) {
		prefsEditor.putString("adddestinationstation", data);
		prefsEditor.commit();
	}

	/**
	 * @param data
	 */
	public void saveRemoveDestinationStationInfo(String data) {
		prefsEditor.putString("removedestinationstation", data);
		prefsEditor.commit();
	}

	/**
	 * @param data
	 */
	public void saveProviderInfo(String data) {
		prefsEditor.putString("provider", data);
		prefsEditor.commit();
	}

	public boolean isLoggedIn() {
		return appSharedPrefs.getBoolean(LOGGED_IN, false);
	}

	public void setLoggedIn(final boolean loggedIn) {
		appSharedPrefs.edit().putBoolean(LOGGED_IN, loggedIn).apply();
	}

	public boolean isFirstLaunch()
	{
		return appSharedPrefs.getBoolean(IS_FIRST_LAUNCH, true);
	}

	public void setisFirstLaunch(boolean isFirstLaunch) {
		appSharedPrefs.edit().putBoolean(IS_FIRST_LAUNCH,isFirstLaunch).apply();
	}

	public void setuserType(String userType) {
		prefsEditor.putString(USER_TYPE, userType).apply();
		prefsEditor.commit();
	}
	public String getUserType(String userType) {
		return appSharedPrefs.getString(USER_TYPE,userType);
	}

	String getString(String var1, String var2) {
		return null;
	}

	public void setRateClickCounter(int rateClickCounter) {
		appSharedPrefs.edit().putInt(RATE_CLICK_COUNTER, rateClickCounter).apply();
	}

	public int getRateClickCounter()
	{
		return appSharedPrefs.getInt(RATE_CLICK_COUNTER, 0);
	}

	public void setIsRead(boolean isRead) {
		appSharedPrefs.edit().putBoolean(IS_READ, isRead).apply();
	}

	public boolean getIsRead() {
		return appSharedPrefs.getBoolean(IS_READ, false);
	}

}
