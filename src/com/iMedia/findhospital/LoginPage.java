package com.iMedia.findhospital;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends Activity implements KeyListener{
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	//TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	AlertDialog alertDialog;
	UserFunctions userFunctions;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
	//	loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				if(userFunctions.isNetworkAvailable(getApplicationContext())){
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				if(email.length()!= 0 &&password.length()!= 0){
				UserFunctions userFunction = new UserFunctions();
				Log.d("Button", "Login");
				JSONObject json = userFunction.loginUser(email, password);

				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
					//	loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							// user successfully logged in
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
							
							// Close Login Screen
							finish();
						}else{
							// Error in login
						//	loginErrorMsg.setText("Incorrect username/password");
							showAlertDialog("Alert","Incorrect Email Address/Password","OK");
							alertDialog.show();
							alertDialog.setCancelable(false);
							
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
				else{
				//	Toast.makeText(getApplicationContext(), "Please fill the details", 2000).show();
					showAlertDialog("Notification","Please fill the details","OK");
				alertDialog.show();
				alertDialog.setCancelable(false);
				}
		}
				else{
					showAlertDialog("Internet Availability","Please Check Your Internet Connection","OK");
					alertDialog.show();
					alertDialog.setCancelable(false);
				}
			}
		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						SignUp.class);
				startActivity(i);
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
	@Override
	public void clearMetaKeyState(View arg0, Editable arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getInputType() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean onKeyDown(View arg0, Editable arg1, int arg2, KeyEvent arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onKeyOther(View arg0, Editable arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onKeyUp(View arg0, Editable arg1, int arg2, KeyEvent arg3) {
		// TODO Auto-generated method stub
		return false;
	}	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    //	Toast.makeText(getApplicationContext(), "bcp", 2000).show();
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
