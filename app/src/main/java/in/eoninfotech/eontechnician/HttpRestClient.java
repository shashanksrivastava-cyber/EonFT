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
//FILE           : HttpRestClient.java
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
//DESCRIPTION    :   This class is used make the connection with the server
//*********************************************************************************************

package in.eoninfotech.eontechnician;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.List;


public class HttpRestClient {

	public static final String tag = "HttpRestClient";
	private static DefaultHttpClient defaultHttpClient = null;

	/**
	 * @return
	 */
	public static DefaultHttpClient getDefaultHttpClient() {
		if (defaultHttpClient == null) {

			boolean trustAll = true;
			ClientConnectionManager clientConnectionManager;
			HttpParams httpParameters;
			// ......
			/*
			 * SchemeRegistry schemeRegistry = new SchemeRegistry();
			 * schemeRegistry.register(new Scheme("http",
			 * PlainSocketFactory.getSocketFactory(), 80));
			 * 
			 * schemeRegistry.register(new Scheme("https", (trustAll ? new
			 * FakeSocketFactory() : new EasySSLSocketFactory()), 443));
			 */
			httpParameters = new BasicHttpParams();

			int timeoutConnection = 4000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			httpParameters.setParameter(
					ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
			httpParameters.setParameter(
					ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
					new ConnPerRouteBean(1));
			httpParameters.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE,
					false);
			// HttpProtocolParams.setUserAgent(httpParameters,
			// "android-client-v1.0");
			// HttpProtocolParams.setVersion(httpParameters,
			// HttpVersion.HTTP_1_1);
			// HttpProtocolParams.setContentCharset(httpParameters, "utf8");
			// clientConnectionManager = new
			// ThreadSafeClientConnManager(httpParameters, schemeRegistry);
			// and later do this
			defaultHttpClient = new DefaultHttpClient(httpParameters);
			/*
			 * defaultHttpClient.setRedirectHandler(new RedirectHandler() {
			 * 
			 * @Override public URI getLocationURI(HttpResponse response,
			 * HttpContext context) throws ProtocolException { return null; }
			 * 
			 * @Override public boolean isRedirectRequested(HttpResponse
			 * response, HttpContext context) { // TODO Auto-generated method
			 * stub return true; } });
			 */

			/*
			 * HttpParams httpParameters = new BasicHttpParams();
			 * 
			 * // Set the timeout in milliseconds until a connection is
			 * established. int timeoutConnection = 3000;
			 * HttpConnectionParams.setConnectionTimeout(httpParameters,
			 * timeoutConnection);
			 * 
			 * // Set the default socket timeout (SO_TIMEOUT) // in milliseconds
			 * which is the timeout for waiting for data. int timeoutSocket =
			 * 5000; HttpConnectionParams.setSoTimeout(httpParameters,
			 * timeoutSocket);
			 * 
			 * httpParameters.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE
			 * , false);
			 * 
			 * 
			 * defaultHttpClient = new DefaultHttpClient(httpParameters);
			 */
			Log.e(tag, " ##### NEW INSTANCE FOR DEFAULT HTTP CLIENT #####");
		} else {
			Log.e(tag, " ##### REUSE FOR DEFAULT HTTP CLIENT #####");
		}

		return defaultHttpClient;
	}

	/*
	 * To convert the InputStream to String we use the BufferedReader.readLine()
	 * method. We iterate until the BufferedReader return null which means
	 * there's no more data to read. Each line will appended to a StringBuilder
	 * and returned as String.
	 */
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/*
	 * This method is used to making the connection with server with HTTP GET
	 * method.
	 * 
	 * @param url
	 * 
	 * @return response
	 * 
	 * @throws IOException
	 */

	public static String connectGet(String url) throws IOException {
		String data = "";
		DefaultHttpClient httpclient;
		HttpGet httpget;
		try {

			// Log.v(tag, "<<<<<()>>>>>" + url);

			httpclient = getDefaultHttpClient();

			// Prepare a request object
			httpget = new HttpGet(url);

			Log.e(tag, "##### " + url);

			// Execute the request
			HttpResponse response;

			response = httpclient.execute(httpget);
			// Examine the response status
			// Log.i(tag,"Http Response: " +
			// response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			switch (response.getStatusLine().getStatusCode()) {

			case HttpURLConnection.HTTP_ACCEPTED:
			case HttpURLConnection.HTTP_OK:
				if (entity != null) {

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					data = convertStreamToString(instream);
					try {
						instream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
				throw new IOException("Network seems busy. Please try later.");
			case HttpURLConnection.HTTP_NOT_FOUND:
				throw new IOException("Server is busy. Please try latter");
			}

		} catch (SocketTimeoutException e) {
			throw new IOException("Network seems busy. Please try later.");
		} catch (ConnectTimeoutException e) {
			throw new IOException(
					"Internet connectivity seems broken. Please check Internet connectivity.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(
					"Internet connectivity seems broken. Please check Internet connectivity.");

		} catch (Exception e) {
			throw new IOException("Error! Please try later.");
		} finally {
			getDefaultHttpClient().getConnectionManager()
					.closeExpiredConnections();
		}

		// Log.v(tag, "<<<<<(Response)>>>>> = " + data);

		// httpclient.getConnectionManager().closeExpiredConnections();
		return data;
	}

	/*
	 * This method is used to making the connection with serverwith HTTP Post
	 * method. Also the send the data to the server.
	 * 
	 * @param url
	 * 
	 * @param parmList
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public static String connectPost(String url, List<NameValuePair> parmList)
			throws IOException {
		Log.e(tag, "<<<<<()>>>>>" + url);

		String data = "";
		HttpClient httpclient = getDefaultHttpClient(); // new
														// DefaultHttpClient();

		// Prepare a request object
		HttpPost httppost = new HttpPost(url);
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parmList);
		httppost.setEntity(formEntity);

		try {

			// Execute the request
			HttpResponse response;
			response = httpclient.execute(httppost);
			// Examine the response status
			Log.e(tag, "Http Response=================");
			Log.e(tag, "Http Response: " + response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			switch (response.getStatusLine().getStatusCode()) {

			case HttpURLConnection.HTTP_ACCEPTED:
			case HttpURLConnection.HTTP_OK:
				if (entity != null) {

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					data = convertStreamToString(instream);
					Log.e(tag, data);
					try {
						instream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
				throw new IOException("Network seems busy. Please try later.");
			case HttpURLConnection.HTTP_NOT_FOUND:
				throw new IOException("Server is busy. Please try latter");
			}

		} catch (SocketTimeoutException e) {
			throw new IOException("Network seems busy. Please try later.");
		} catch (ConnectTimeoutException e) {
			throw new IOException(
					"Internet connectivity seems broken. Please check Internet connectivity.");
		} catch (IOException e) {
			throw new IOException("Server is busy. Please try latter");

		} catch (Exception e) {
			throw new IOException("Error! Please try later.");
		} finally {
			getDefaultHttpClient().getConnectionManager()
					.closeExpiredConnections();
		}

		Log.e(tag, "<<<<<(Response)>>>>> = " + data);
		// httpclient.getConnectionManager().closeExpiredConnections();
		return data;
	}

	/*
	 * This method is used to making the connection with server with HTTP POST
	 * method.
	 * 
	 * @param url
	 * 
	 * @return response
	 * 
	 * @throws IOException
	 */
	public static String connectPost(String url) throws IOException {
		Log.e(tag, "<<<<<()>>>>>" + url);

		String data = "";
		HttpClient httpclient = getDefaultHttpClient(); // new
														// DefaultHttpClient();

		// Prepare a request object
		HttpPost httppost = new HttpPost(url);

		try {

			// Execute the request
			HttpResponse response;
			response = httpclient.execute(httppost);
			// Examine the response status
			Log.e(tag, "Http Response=================");
			Log.e(tag, "Http Response: " + response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			switch (response.getStatusLine().getStatusCode()) {

			case HttpURLConnection.HTTP_ACCEPTED:
			case HttpURLConnection.HTTP_OK:
				if (entity != null) {

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					data = convertStreamToString(instream);
					Log.e(tag, data);
					try {
						instream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
				throw new IOException("Network seems busy. Please try later.");
			case HttpURLConnection.HTTP_NOT_FOUND:
				throw new IOException("Server is busy. Please try latter");
			}

		} catch (SocketTimeoutException e) {
			throw new IOException("Network seems busy. Please try later.");
		} catch (ConnectTimeoutException e) {
			throw new IOException(
					"Internet connectivity seems broken. Please check Internet connectivity.");
		} catch (IOException e) {
			throw new IOException("Server is busy. Please try latter");

		} catch (Exception e) {
			throw new IOException("Error! Please try later.");
		} finally {
			getDefaultHttpClient().getConnectionManager()
					.closeExpiredConnections();
		}

		Log.e(tag, "<<<<<(Response)>>>>> = " + data);
		// httpclient.getConnectionManager().closeExpiredConnections();
		return data;
	}

}
