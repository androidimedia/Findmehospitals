package com.iMedia.findhospital;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MapNearestHospital extends Activity implements OnClickListener{
	GoogleMap googleMap;
	ArrayList<LatLng> markerPoints;
	TextView tvDistanceDuration;
	double currentLatitude = 0;
	double currentLongitude = 0;
	private String jsonResult;
	private String url = "http://imediahost.com/imedia_android/include/location_details.php";
	private ListView listView;
	GPSTracker gps;
	UserFunctions userFunctions;
	// ArrayList<Location> latlongList = new ArrayList<Location>();
	ArrayList<Double> latList = new ArrayList<Double>();
	ArrayList<Double> longList = new ArrayList<Double>();
	ImageButton btnHome,btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_map_nearest_hospital);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			currentLatitude = extras.getDouble("paramLat");
			currentLongitude = extras.getDouble("paramLong");
			Log.i("bundelLat", String.valueOf(currentLatitude));
			Log.i("bundelLong", String.valueOf(currentLongitude));
		} else {
			// ..oops!
			Toast.makeText(getApplicationContext(), "bundle null", 1000).show();
			Log.i("bundel", "sorry");
		}
		userFunctions = new UserFunctions();
		btnHome= (ImageButton) findViewById(R.id.button12);
		btnLogout= (ImageButton) findViewById(R.id.button13);
		btnHome.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setClickable(true);
		accessWebService();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		
		case R.id.button12:
			Intent newActivity12 = new Intent(this, DashboardActivity.class);
			newActivity12.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newActivity12);
//            finish();
			break;
		case R.id.button13:
			userFunctions.logoutUser(getApplicationContext());
			Intent login = new Intent(getApplicationContext(),
					LoginPage.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
			break;

		default:
			break;
		}
	}

	// this.tvDistanceDuration = (TextView) this
	// .findViewById(R.id.tv_distance_time);
	// // Initializing
	// this.markerPoints = new ArrayList<LatLng>();
	//
	// if (googleMap == null) {
	// googleMap = ((MapFragment) getFragmentManager().findFragmentById(
	// R.id.map)).getMap();
	// // Getting reference to SupportMapFragment of the activity_main
	// // SupportMapFragment fm = (SupportMapFragment)
	// // this.getSupportFragmentManager().findFragmentById(R.id.map);
	//
	// // Getting Map for the SupportMapFragment
	// // this.map = fm.getMap();
	//
	// // Enable MyLocation Button in the Map
	// this.googleMap.setMyLocationEnabled(true);
	//
	// // Setting onclick event listener for the map
	// this.googleMap.setOnMapClickListener(new OnMapClickListener() {
	// @Override
	// public void onMapClick(LatLng point) {
	// // Already two locations
	// if (MapNearestHospital.this.markerPoints.size() > 1) {
	// MapNearestHospital.this.markerPoints.clear();
	// MapNearestHospital.this.googleMap.clear();
	// }
	//
	// // Adding new item to the ArrayList
	// MapNearestHospital.this.markerPoints.add(point);
	//
	// // Creating MarkerOptions
	// MarkerOptions options = new MarkerOptions();
	//
	// // Setting the position of the marker
	// options.position(point);
	//
	// /**
	// * For the start location, the color of marker is GREEN and
	// * for the end location, the color of marker is RED.
	// */
	// if (MapNearestHospital.this.markerPoints.size() == 1) {
	// options.icon(BitmapDescriptorFactory
	// .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	// } else if (MapNearestHospital.this.markerPoints.size() == 2) {
	// options.icon(BitmapDescriptorFactory
	// .defaultMarker(BitmapDescriptorFactory.HUE_RED));
	// }
	//
	// // Add new marker to the Google Map Android API V2
	// MapNearestHospital.this.googleMap.addMarker(options);
	//
	// // Checks, whether start and end locations are captured
	// if (MapNearestHospital.this.markerPoints.size() >= 2) {
	// LatLng origin = MapNearestHospital.this.markerPoints
	// .get(0);
	// LatLng dest = MapNearestHospital.this.markerPoints
	// .get(1);
	//
	// // Getting URL to the Google Directions API
	// String url = MapNearestHospital.this.getDirectionsUrl(
	// origin, dest);
	//
	// DownloadTask downloadTask = new DownloadTask();
	//
	// // Start downloading json data from Google Directions
	// // API
	// downloadTask.execute(url);
	// }
	// }
	// });
	// }
	// }
	//
	// private String getDirectionsUrl(LatLng origin, LatLng dest) {
	// // Origin of route
	// String str_origin = "origin=" + origin.latitude + ","
	// + origin.longitude;
	//
	// // Destination of route
	// String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
	//
	// // Sensor enabled
	// String sensor = "sensor=false";
	//
	// // Building the parameters to the web service
	// String parameters = str_origin + "&" + str_dest + "&" + sensor;
	//
	// // Output format
	// String output = "json";
	//
	// // Building the url to the web service
	// String url = "https://maps.googleapis.com/maps/api/directions/"
	// + output + "?" + parameters;
	//
	// return url;
	// }
	//
	// /** A method to download json data from url */
	// private String downloadUrl(String strUrl) throws IOException {
	// String data = "";
	// InputStream iStream = null;
	// HttpURLConnection urlConnection = null;
	// try {
	// URL url = new URL(strUrl);
	//
	// // Creating an http connection to communicate with url
	// urlConnection = (HttpURLConnection) url.openConnection();
	//
	// // Connecting to url
	// urlConnection.connect();
	//
	// // Reading data from url
	// iStream = urlConnection.getInputStream();
	//
	// BufferedReader br = new BufferedReader(new InputStreamReader(
	// iStream));
	//
	// StringBuffer sb = new StringBuffer();
	//
	// String line = "";
	// while ((line = br.readLine()) != null) {
	// sb.append(line);
	// }
	//
	// data = sb.toString();
	//
	// br.close();
	//
	// } catch (Exception e) {
	// Log.d("Exception while downloading url", e.toString());
	// } finally {
	// iStream.close();
	// urlConnection.disconnect();
	// }
	// return data;
	// }
	//
	// // Fetches data from url passed
	// private class DownloadTask extends AsyncTask<String, Void, String> {
	// // Downloading data in non-ui thread
	// @Override
	// protected String doInBackground(String... url) {
	//
	// // For storing data from web service
	// String data = "";
	//
	// try {
	// // Fetching the data from web service
	// data = MapNearestHospital.this.downloadUrl(url[0]);
	// } catch (Exception e) {
	// Log.d("Background Task", e.toString());
	// }
	// return data;
	// }
	//
	// // Executes in UI thread, after the execution of
	// // doInBackground()
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	//
	// ParserTask parserTask = new ParserTask();
	//
	// // Invokes the thread for parsing the JSON data
	// parserTask.execute(result);
	//
	// }
	// }
	//
	// /** A class to parse the Google Places in JSON format */
	// private class ParserTask extends
	// AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
	//
	// // Parsing the data in non-ui thread
	// @Override
	// protected List<List<HashMap<String, String>>> doInBackground(
	// String... jsonData) {
	// JSONObject jObject;
	// List<List<HashMap<String, String>>> routes = null;
	//
	// try {
	// jObject = new JSONObject(jsonData[0]);
	// DirectionsJSONParser parser = new DirectionsJSONParser();
	//
	// // Starts parsing data
	// routes = parser.parse(jObject);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return routes;
	// }
	//
	// // Executes in UI thread, after the parsing process
	// @Override
	// protected void onPostExecute(List<List<HashMap<String, String>>> result)
	// {
	// ArrayList<LatLng> points = null;
	// PolylineOptions lineOptions = null;
	// MarkerOptions markerOptions = new MarkerOptions();
	// String distance = "";
	// String duration = "";
	//
	// if (result.size() < 1) {
	// Toast.makeText(MapNearestHospital.this.getBaseContext(),
	// "No Points", Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// // Traversing through all the routes
	// for (int i = 0; i < result.size(); i++) {
	// points = new ArrayList<LatLng>();
	// lineOptions = new PolylineOptions();
	//
	// // Fetching i-th route
	// List<HashMap<String, String>> path = result.get(i);
	//
	// // Fetching all the points in i-th route
	// for (int j = 0; j < path.size(); j++) {
	// HashMap<String, String> point = path.get(j);
	//
	// if (j == 0) { // Get distance from the list
	// distance = point.get("distance");
	// continue;
	// } else if (j == 1) { // Get duration from the list
	// duration = point.get("duration");
	// continue;
	// }
	// double lat = Double.parseDouble(point.get("lat"));
	// double lng = Double.parseDouble(point.get("lng"));
	// LatLng position = new LatLng(lat, lng);
	// points.add(position);
	// }
	//
	// // Adding all the points in the route to LineOptions
	// lineOptions.addAll(points);
	// lineOptions.width(2);
	// lineOptions.color(Color.RED);
	// }
	//
	// MapNearestHospital.this.tvDistanceDuration.setText("Distance:"
	// + distance + ", Duration:" + duration);
	//
	// // Drawing polyline in the Google Map for the i-th route
	// MapNearestHospital.this.googleMap.addPolyline(lineOptions);
	// }
	// }

	// Async Task to access the web
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
	public void ListDrwaer() {
		List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

		try {
			latList.clear(); // clear the all data from latlist
			longList.clear(); // clear the all data from longlist
			Log.i("main12", "try");
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("location_info");
			// typedEmail = loginId.getText().toString().trim();
			// typedPassword = loginPassword.getText().toString().trim();
			for (int i = 0; i < jsonMainNode.length(); i++) {
				Log.i("main13", "for");
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				// String number = jsonChildNode.optString("employee_no");
				String targetLat = jsonChildNode.optString("latitude").trim();
				String targetLong = jsonChildNode.optString("longitude").trim();
				Log.i("name", targetLat);
				Log.i("password", targetLong);
				double targetLats = Double.parseDouble(targetLat);
				double targetLongs = Double.parseDouble(targetLong);
				Location mylocation = new Location("");
				Location dest_location = new Location("");
				mylocation.setLatitude(currentLatitude);
				mylocation.setLongitude(currentLongitude);
				dest_location.setLatitude(targetLats);
				dest_location.setLongitude(targetLongs);
				float distance = (mylocation.distanceTo(dest_location)) / 1000;// in
																				// kilometers
				// Toast.makeText(this, "Distance" + Float.toString(distance),
				// Toast.LENGTH_LONG).show();
			//	if (distance <= 10) {
					Log.i("status", "success");
					String l_n = jsonChildNode.optString("location_name");
					String dis = String.valueOf(distance);
					Log.i("check", String.valueOf(distance));
					String outPut ="Location Name : " + l_n + "\n"+"Distance : " + dis;
					employeeList.add(createEmployee("employees", outPut));
					// latlongList.add(dest_location);
					latList.add(targetLats);
					longList.add(targetLongs);
			//	}
			}
		} catch (JSONException e) {
			Log.i("main14", e.toString());
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
				android.R.layout.simple_list_item_1,
				new String[] { "employees" }, new int[] { android.R.id.text1 });
		Log.i("main16", "sa");
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				// Toast.makeText(getApplicationContext(), ((TextView)
				// view).getText(),
				// Toast.LENGTH_SHORT).show();
				double fromLatitude = latList.get(position);
				double fromLongitude = longList.get(position);
				Log.i("firstlat", String.valueOf(fromLatitude));
				Log.i("firstlong", String.valueOf(fromLongitude));
				double toLatitude = 0;
				double toLongitude = 0;
				gps = new GPSTracker(MapNearestHospital.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {
					toLatitude = gps.getLatitude();
					toLongitude = gps.getLongitude();
					// \n is for new line
					// Toast.makeText(getApplicationContext(),
					// "Your last Location is - \nLat: " + toLatitude +
					// "\nLong: " + toLongitude, Toast.LENGTH_LONG).show();
					Log.i("lastlat", String.valueOf(toLatitude));
					Log.i("lastlong", String.valueOf(toLongitude));
					 Intent in2 = new Intent(getApplicationContext(),
							 com.iMedia.findhospital.Map.class);
							 in2.putExtra("firstLat", toLatitude );
							 in2.putExtra( "firstLong", toLongitude );
							 in2.putExtra( "lastLat", fromLatitude );
							 in2.putExtra( "lastLong", fromLongitude );
							 Log.i("check", "start");
							 startActivity(in2);
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}
				
//				 Intent in2 = new Intent(getApplicationContext(),
//				 com.iMedia.findhospital.Map.class);
//				 in2.putExtra("firstLat", toLatitude );
//				 in2.putExtra( "firstLong", toLongitude );
//				 in2.putExtra( "lastLat", fromLatitude );
//				 in2.putExtra( "lastLong", fromLongitude );
//				 Log.i("check", "start");
//				 startActivity(in2);

			}

		});
	}

	private HashMap<String, String> createEmployee(String name, String number) {
		Log.i("main15", "hm");
		HashMap<String, String> employeeNameNo = new HashMap<String, String>();
		employeeNameNo.put(name, number);
		return employeeNameNo;
	}
}
