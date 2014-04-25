package com.iMedia.findhospital;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.iMedia.findhospital.R.color;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FirstAid extends Activity implements OnClickListener {

	//Button one, two, three, four, five, six, seven, eight, nine, ten, eleven;
	UserFunctions userFunctions;
	ImageButton btnHome,btnLogout;
	private String jsonResult;
	private String url = "http://imediahost.com/imedia_android/include/tips_details.php";
	//private ListView listView;
	//GPSTracker gps;
	// ArrayList<Location> latlongList = new ArrayList<Location>();
	ArrayList<String> tipDesc = new ArrayList<String>();
	ArrayList<String> tipTitle = new ArrayList<String>();
	ArrayList<String> imageUrl = new ArrayList<String>();
	AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(userFunctions.isNetworkAvailable(getApplicationContext())){
		userFunctions = new UserFunctions();		
		setContentView(R.layout.activity_first_aid);		
		btnHome= (ImageButton) findViewById(R.id.button12);
		btnLogout= (ImageButton) findViewById(R.id.button13);
		btnHome.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		accessWebService();
		}
		else{
			//Toast.makeText(getApplicationContext(), "nt", 1000).show();
			showAlertDialog("Internet Availability","Please Check Your Internet Connection","OK");
			alertDialog.show();
			alertDialog.setCancelable(false);
			
		}

		
	}
	public AlertDialog showAlertDialog(String title,String message,String buttonName)
	{
		alertDialog=new AlertDialog.Builder(this).setTitle(title).setIcon(R.drawable.logo_fh).setMessage(message).setNeutralButton(buttonName,new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				alertDialog.cancel();
				//alertDialog.setCanceledOnTouchOutside(true);
				finish();
			}
		})./*setNegativeButton("No",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ad.cancel();
			}
		}).*/
		create();
		return alertDialog;
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		
		case R.id.button12:
			if(userFunctions.isNetworkAvailable(getApplicationContext())){
			Intent newActivity12 = new Intent(this, DashboardActivity.class);
			newActivity12.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newActivity12);
//            finish();
			}
			else{
				//Toast.makeText(getApplicationContext(), "nt", 1000).show();
				showAlertDialog("Internet Availability","Please Check Your Internet Connection","OK");
				alertDialog.show();
				alertDialog.setCancelable(false);
				
			}
			break;
		case R.id.button13:
			if(userFunctions.isNetworkAvailable(getApplicationContext())){
			userFunctions.logoutUser(getApplicationContext());
			Intent login = new Intent(getApplicationContext(),
					LoginPage.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
		}
		else{
			//Toast.makeText(getApplicationContext(), "nt", 1000).show();
			showAlertDialog("Internet Availability","Please Check Your Internet Connection","OK");
			alertDialog.show();
			alertDialog.setCancelable(false);
			
		}
			break;

		default:
			break;
		}
	}
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			Log.i("main1", "doInBackground");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			try {
				Log.i("main2", "try");
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				Log.i("main3", e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("main4", e.toString());
				e.printStackTrace();
			} catch (Exception e) {
				Log.i("main5", e.toString());
				e.printStackTrace();
			}
			return null;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			Log.i("main6", "sb");
			try {
				while ((rLine = rd.readLine()) != null) {
					Log.i("main7", "while");
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				Log.i("main8", e.toString());
				// e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Log.i("main9", e.toString());
				// e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("main10", "onpost");
			ListDrwaer();
		}
	}// end async task

	public void accessWebService() {
		JsonReadTask task = new JsonReadTask();
		Log.i("main11", "accessweb");
		// passes values for the urls string array
		task.execute(new String[] { url });
	}

	// build hash set for list view
	@SuppressLint("ResourceAsColor")
	public void ListDrwaer() {
	//	List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

		try {
			tipDesc.clear(); // clear the all data from latlist
			//longList.clear(); // clear the all data from longlist
			Log.i("main12", "try");
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("tipsWithDescription");
			final LinearLayout lm = (LinearLayout) findViewById(R.id.container);
	         
	        // create the layout params that will be used to define how your
	        // button will be displayed
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        params.setMargins(10, 20, 20, 10);
			for (int i = 0; i < jsonMainNode.length(); i++) {
				Log.i("main13", "for");
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);				
				String tipsTitle = jsonChildNode.optString("tips_title").trim();
				String tipsDescription = jsonChildNode.optString("tips_description").trim();
				String imageURL = jsonChildNode.optString("image_url").trim();
				tipDesc.add(tipsDescription);
				tipTitle.add(tipsTitle);
				imageUrl.add(imageURL);
				// Create LinearLayout
	            LinearLayout ll = new LinearLayout(this);
	            ll.setOrientation(LinearLayout.HORIZONTAL);
	             	             
	            // Create Button
	            final Button btn = new Button(this);
	                // Give button an ID
	                btn.setId(i+1);
	                                
	                btn.setText(tipsTitle);
	                btn.setBackgroundResource(R.drawable.color_sky_blue);
	               
	                // set the layoutParams on the button
	                btn.setLayoutParams(params);
	                 
	                final int index = i;
	                // Set click listener for button
	                btn.setOnClickListener(new OnClickListener() {
	                    public void onClick(View v) {
	                         
	                        Log.i("TAG", "index :" + index);
	                         
//	                        Toast.makeText(getApplicationContext(), 
//	                                "Clicked Button Index :" + index, 
//	                                Toast.LENGTH_LONG).show();
	                        Intent in2 = new Intent(getApplicationContext(),
	               				 com.iMedia.findhospital.ShowFirstAid.class);
	               				 in2.putExtra("tipdescn", tipDesc.get(index) );
	               				in2.putExtra("tiptitle", tipTitle.get(index) );
	               				in2.putExtra("imageurl", imageUrl.get(index) );
	               				 Log.i("check", tipDesc.get(index));
	               				 startActivity(in2);
	                    }
	                });
	                 
	               //Add button to LinearLayout
	                ll.addView(btn);
	               //Add button to LinearLayout defined in XML
	                lm.addView(ll);  
			}
		} catch (JSONException e) {
			Log.i("main14", e.toString());
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}

//		SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
//				android.R.layout.simple_list_item_1,
//				new String[] { "employees" }, new int[] { android.R.id.text1 });
//		Log.i("main16", "sa");
		
	}

//	private HashMap<String, String> createEmployee(String name, String number) {
//		Log.i("main15", "hm");
//		HashMap<String, String> employeeNameNo = new HashMap<String, String>();
//		employeeNameNo.put(name, number);
//		return employeeNameNo;
//	}
}


