package com.iMedia.findhospital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends Activity {
	UserFunctions userFunctions;
	Button btnFirstAid, btnFindhospital, btnLogout;
	GPSTracker gps;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Dashboard Screen for the application
		 * */
		// Check login status in database
		if (userFunctions.isNetworkAvailable(getApplicationContext())) {
			// Toast.makeText(getApplicationContext(), "net", 1000).show();
			userFunctions = new UserFunctions();
			if (userFunctions.isUserLoggedIn(getApplicationContext())) {
				setContentView(R.layout.activity_dashboard);
				btnFirstAid = (Button) findViewById(R.id.button1);
				btnFindhospital = (Button) findViewById(R.id.button2);
				btnLogout = (Button) findViewById(R.id.button3);
				btnFirstAid.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// userFunctions.logoutUser(getApplicationContext());
						Intent in1 = new Intent(getApplicationContext(),
								FirstAid.class);
						// in1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in1);

					}
				});
				btnFindhospital.setOnClickListener(new View.OnClickListener() {
					double latitude = 0;
					double longitude = 0;

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (userFunctions.isNetworkAvailable(getApplicationContext())) {
						gps = new GPSTracker(DashboardActivity.this);
						// check if GPS enabled
						if (gps.canGetLocation()) {
							latitude = gps.getLatitude();
							longitude = gps.getLongitude();
							Intent in2 = new Intent(getApplicationContext(),
									MapNearestHospital.class);
							in2.putExtra("paramLat", latitude);
							in2.putExtra("paramLong", longitude);
							startActivity(in2);
							// \n is for new line
							// Toast.makeText(getApplicationContext(),
							// "Your Location is - \nLat: " + latitude +
							// "\nLong: " + longitude,
							// Toast.LENGTH_LONG).show();
						} else {
							// can't get location
							// GPS or Network is not enabled
							// Ask user to enable GPS/network in settings
							gps.showSettingsAlert();
						}

						// Intent in2 = new Intent(getApplicationContext(),
						// MapNearestHospital.class);
						// in2.putExtra( "paramLat", latitude );
						// in2.putExtra( "paramLong", longitude );
						// startActivity(in2);

					}
						else {
							// Toast.makeText(getApplicationContext(), "nt", 1000).show();
							showAlertDialog("Internet Availability",
									"Please Check Your Internet Connection", "OK");
							alertDialog.show();
							alertDialog.setCancelable(false);

						}
					}
				});
				btnLogout.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						userFunctions.logoutUser(getApplicationContext());
						Intent login = new Intent(getApplicationContext(),
								LoginPage.class);
						login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(login);
						// Closing dashboard screen
						finish();

					}
				});

			} else {
				// user is not logged in show login screen
				Intent login = new Intent(getApplicationContext(),
						LoginPage.class);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				// Closing dashboard screen
				finish();
			}
		}

		else {
			// Toast.makeText(getApplicationContext(), "nt", 1000).show();
			showAlertDialog("Internet Availability",
					"Please Check Your Internet Connection", "OK");
			alertDialog.show();
			alertDialog.setCancelable(false);

		}

	}

	public AlertDialog showAlertDialog(String title, String message,
			String buttonName) {
		alertDialog = new AlertDialog.Builder(this)
				.setTitle(title)
				.setIcon(R.drawable.logo_fh)
				.setMessage(message)
				.setNeutralButton(buttonName,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								alertDialog.cancel();
								// alertDialog.setCanceledOnTouchOutside(true);
								finish();
							}
						})./*
							 * setNegativeButton("No",new
							 * DialogInterface.OnClickListener() {
							 * 
							 * @Override public void onClick(DialogInterface
							 * dialog, int which) { // TODO Auto-generated
							 * method stub ad.cancel(); } }).
							 */
				create();
		return alertDialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			userFunctions.logoutUser(getApplicationContext());
			Intent login = new Intent(getApplicationContext(), LoginPage.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}