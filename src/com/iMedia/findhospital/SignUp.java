package com.iMedia.findhospital;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity  {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	//TextView registerErrorMsg;
//	LoginPage loginPage;
	AlertDialog alertDialog;
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	UserFunctions userFunctions;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		//registerErrorMsg = (TextView) findViewById(R.id.register_error);
		
		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				if (userFunctions.isNetworkAvailable(getApplicationContext())) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				if(name.length()!=0 && email.length()!= 0 && password.length()!= 0){
				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.registerUser(name, email, password);
				
				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						//registerErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							// user successfully registred
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");
							
							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
							// Launch Dashboard Screen
							Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
							// Close all views before launching Dashboard
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							// Close Registration Screen
							finish();
						}else{
							// Error in registration
							//registerErrorMsg.setText("Error occured in registration");
							showAlertDialog("Error","Error occured in registration","OK");
							alertDialog.show();
							alertDialog.setCancelable(false);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				else{
					//Toast.makeText(getApplicationContext(), "Please fill the details", 2000).show();
					showAlertDialog("Notification","Please fill the details","OK");
				alertDialog.show();
				alertDialog.setCancelable(false);
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
			
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginPage.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}
	public AlertDialog showAlertDialog(String title,String message,String buttonName)
	{
		alertDialog=new AlertDialog.Builder(this).setTitle(title).setIcon(R.drawable.logo_fh).setMessage(message).setNeutralButton(buttonName,new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				alertDialog.cancel();
				//finish();
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
}
