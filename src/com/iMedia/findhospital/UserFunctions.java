/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.iMedia.findhospital;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String loginURL = "http://imediahost.com/imedia_android/";
	private static String registerURL = "http://imediahost.com/imedia_android/";
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		 Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make Login Request
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	/*public static boolean isNetworkAvailable(Context context) 
	{
	    ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

	    if (connectivity != null) 
	    {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();

	        if (info != null) 
	        {
	            for (int i = 0; i < info.length; i++) 
	            {
	                Log.i("Class", info[i].getState().toString());
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) 
	                {
	                    return true;
	                }
	            }
	        }
	    }
	    return false;
	} */ // code for only check your network
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	    	try {
	            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 2 www.google.com");
	            int returnVal = p1.waitFor();
	            boolean reachable = (returnVal==0);
	            return reachable;
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	
}
