package com.iMedia.findhospital;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class ShowFirstAid extends Activity implements OnClickListener{
	TextView txtShowAid,txtShowTitle;
//	string myParam = 0;
	String textDescContent="";
	String textTitleContent="";
	String imageUrlContent="";
	ImageButton btnHome,btnLogout;
	ImageView firstAidImage;
	UserFunctions userFunctions;
	AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(userFunctions.isNetworkAvailable(getApplicationContext())){
		setContentView(R.layout.activity_show_first_aid);
		userFunctions = new UserFunctions();
		btnHome= (ImageButton) findViewById(R.id.button12);
		btnLogout= (ImageButton) findViewById(R.id.button13);
		firstAidImage=(ImageView)findViewById(R.id.imageView1);
		
		btnHome.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		txtShowAid = (TextView) findViewById(R.id.titleDescn);
		txtShowTitle= (TextView) findViewById(R.id.titleText);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			textDescContent = extras.getString("tipdescn");
			textTitleContent=extras.getString("tiptitle");
			imageUrlContent=extras.getString("imageurl");
			Log.i("bundel", String.valueOf(textDescContent));
			Log.i("bundel", String.valueOf(textTitleContent));
			Log.i("bundel", imageUrlContent);
		} else {
			// ..oops!
			txtShowAid.setText("try again");
			Log.i("bundel", "sorry");
		}
		if(imageUrlContent!=null){
		firstAidImage.setImageBitmap(getBitmapFromURL(imageUrlContent));
		}
		txtShowAid.setText(textDescContent);
		txtShowTitle.setText(textTitleContent);
//		switch (myParam) {
//		case 1:
//			txtShowAid
//					.setText("what is Lorem Ipsum : Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//			break;
//		case 2:
//			txtShowAid
//					.setText("why do we use : It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).");
//			break;
//		case 3:
//			txtShowAid
//					.setText("where does it comes from:Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of ,de Finibus Bonorum et Malorum(The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.., comes from a line in section 1.10.32.The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from :de Finibus Bonorum et Malorum. by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.");
//			break;
//		case 4:
//			txtShowAid
//					.setText("where can i get some:There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.");
//			break;
//		case 5:
//			txtShowAid
//					.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.");
//			break;
//		case 6:
//			txtShowAid
//					.setText("6666 6666 66666666 66666666666 66666666 66666666 666666666 666666666 666666");
//			break;
//		case 7:
//			txtShowAid
//					.setText("aepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo");
//			break;
//
//		default:
//			txtShowAid
//			.setText("Default aepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo");
//			break;
//		}

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
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        Log.e("src",src);
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        Log.e("Bitmap","returned");
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("Exception",e.getMessage());
	        return null;
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_first_aid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		
		case R.id.button12:
			if (userFunctions.isNetworkAvailable(getApplicationContext())) {
			Intent newActivity12 = new Intent(this, DashboardActivity.class);
			newActivity12.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newActivity12);
//            finish();
			}
			else {
				// Toast.makeText(getApplicationContext(), "nt", 1000).show();
				showAlertDialog("Internet Availability",
						"Please Check Your Internet Connection", "OK");
				alertDialog.show();
				alertDialog.setCancelable(false);

			}
            
			break;
		case R.id.button13:
			if (userFunctions.isNetworkAvailable(getApplicationContext())) {
			userFunctions.logoutUser(getApplicationContext());
			Intent login = new Intent(getApplicationContext(),
					LoginPage.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
			}
			else {
				// Toast.makeText(getApplicationContext(), "nt", 1000).show();
				showAlertDialog("Internet Availability",
						"Please Check Your Internet Connection", "OK");
				alertDialog.show();
				alertDialog.setCancelable(false);

			}
			
			break;

		default:
			break;
		}
	}

	


}
