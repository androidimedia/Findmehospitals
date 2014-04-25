package com.iMedia.findhospital;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

public class Map extends FragmentActivity implements OnClickListener{
	double currentLatitude = 0;
	double currentLongitude = 0;
	double lastLatitude = 0;
	double lastLongitude = 0;
//	List<Overlay> mapOverlays;
//    GeoPoint point1, point2;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    GoogleMap mGoogleMap;
    MarkerOptions markerOptions;
    Location location ;
    ImageButton btnHome,btnLogout;
    UserFunctions userFunctions;
    AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(userFunctions.isNetworkAvailable(getApplicationContext())){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_map);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			currentLatitude = extras.getDouble("firstLat");
			currentLongitude = extras.getDouble("firstLong");
			lastLatitude = extras.getDouble("lastLat");
			lastLongitude = extras.getDouble("lastLong");
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
		
		 v2GetRouteDirection = new GMapV2GetRouteDirection();
         SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
           mGoogleMap = supportMapFragment.getMap();

           // Enabling MyLocation in Google Map
           mGoogleMap.setMyLocationEnabled(true);
           mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
           mGoogleMap.getUiSettings().setCompassEnabled(true);
           mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
           mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
           mGoogleMap.setTrafficEnabled(true);
           mGoogleMap.clear(); 
           LatLng latLng = new LatLng(lastLatitude, lastLongitude);
           mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
           mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
           mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("You are here!"));
           mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); 
           markerOptions = new MarkerOptions();
           fromPosition = new LatLng(currentLatitude, currentLongitude);
           toPosition = new LatLng(lastLatitude, lastLongitude);
           GetRouteTask getRoute = new GetRouteTask();
           getRoute.execute();
		
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
				alertDialog.setCancelable(false);
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

	

	private class GetRouteTask extends AsyncTask<String, Void, String> {
        
        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
              Dialog = new ProgressDialog(Map.this);
              Dialog.setMessage("Loading route...");
              Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
              //Get All Route values
                    document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
                    response = "Success";
              return response;

        }

        @Override
        protected void onPostExecute(String result) {
              mGoogleMap.clear();
              if(response.equalsIgnoreCase("Success")){
              ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
              PolylineOptions rectLine = new PolylineOptions().width(10).color(
                          Color.RED);

              for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
              }
              // Adding route on the map
              mGoogleMap.addPolyline(rectLine);
              markerOptions.position(toPosition);
              markerOptions.draggable(true);
              mGoogleMap.addMarker(markerOptions);

              }
             
              Dialog.dismiss();
        }
  }
  @Override
  protected void onStop() {
        super.onStop();
        finish();
  }


}
