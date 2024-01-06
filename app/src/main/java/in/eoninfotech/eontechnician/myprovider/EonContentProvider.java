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
//FILE           : EonContentProvider.java
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
//DESCRIPTION    :  This code is used to create the database for Punbus.
//
//*********************************************************************************************

package in.eoninfotech.eontechnician.myprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.HashMap;
/***************************************************************************/
// Copyright EON Infotech Ltd., published work, created 2017.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/

public class EonContentProvider extends ContentProvider {
	/*
	 * This class is used to create the database table and also implement the
	 * insert, delete, update etc operations.
	 */

	public static final String DATABASE_NAME = "fielddata.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "tb_punbus_info";
	public static final int TABLE_ID = 1;
	public static final String AUTHORITY = "in.eoninfotech.eontechnician.myprovider.EonContentProvider";
	private static final UriMatcher uriMatcher;
	private static final HashMap<String, String> ctuData;

	private Context context;
	private DbHelper DBHelper;


	/*
	 * static block is used to intialize the UriMatcher and HashTable .
	 */
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, TABLE_NAME, TABLE_ID);

		ctuData = new HashMap<String, String>();
		ctuData.put(CtuColumn._ID, CtuColumn._ID);
		ctuData.put(CtuColumn.PUNBUS_STATION_ID,
				CtuColumn.PUNBUS_STATION_ID);
		ctuData.put(CtuColumn.PUNBUS_STATION_NAME,
				CtuColumn.PUNBUS_STATION_NAME);
	}

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database
		 * .sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
				db.beginTransaction();
				StringBuilder createQuery = new StringBuilder();
				createQuery = new StringBuilder();
				createQuery.append("CREATE TABLE ");
				createQuery.append(TABLE_NAME);
				createQuery.append(" ( ");
				createQuery.append(CtuColumn._ID);
				createQuery.append(" integer primary key , ");
				createQuery.append(CtuColumn.PUNBUS_STATION_NAME);
				createQuery.append(" VARCHAR(250), ");
				createQuery.append(CtuColumn.PUNBUS_STATION_ID);
				createQuery.append(" VARCHAR(50) ");
				createQuery.append(" ); ");
				db.execSQL(createQuery.toString());

				Log.e("", "<<<<CREATING THE TABLE SUCCESSFULLY>>>>");
				db.setTransactionSuccessful();

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("", "Error  " + e.getMessage());
			} finally {
				db.endTransaction();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
		 * .sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			try {

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
				onCreate(db);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	DbHelper dbHelper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	public boolean onCreate() {

		dbHelper = new DbHelper(getContext());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case TABLE_ID:

			count = database.delete(TABLE_NAME, selection, selectionArgs);
			Log.e("", "the number of file delete " + count);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return count;
	}
/*	public boolean deletedata(){
		int count = 0;
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		count= database.delete(TABLE_NAME, null, null) ;
		Log.i("***deleted**", String.valueOf(count));
		return count>0;
	}*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case TABLE_ID:
			return CtuColumn.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {

		Uri tempUri = null;
		ContentValues values;
		if (initialValues == null) {
			values = new ContentValues();
		} else {
			values = new ContentValues(initialValues);
		}

		long id;
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case TABLE_ID:

			id = database.insert(TABLE_NAME, null, values);
			Log.e("iddd", "iddddddddddddd" + id);

			if (id > -1) {
				tempUri = ContentUris.withAppendedId(CtuColumn.CONTENT_URI,
						id);
				// getContext().getContentResolver().notifyChange(tempUri,
				// null);

				Log.i(getClass().getSimpleName(), "Inserted : " + id);

				return tempUri;
			}

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case TABLE_ID:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(ctuData);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase database = dbHelper.getReadableDatabase();

		// (database, projection, selection, selectionArgs,

		Cursor c = qb.query(database, projection, selection, selectionArgs,
				null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case TABLE_ID:
			count = database.update(TABLE_NAME, values, selection,
					selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (count > 0) {
			// getContext().getContentResolver().notifyChange(uri, null);
			Log.i(getClass().getSimpleName(), "Updated : " + count);
		}
		return count;
	}

	/* this class is define the column name */

	public static class CtuColumn implements BaseColumns {

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);
		public static final String PUNBUS_STATION_NAME = "station_name";
		public static final String PUNBUS_STATION_ID = "station_id";
		public static final String CONTENT_TYPE = "in.eoninfotech.eontechnician.myprovider/CTU_DATA";

	}

}
