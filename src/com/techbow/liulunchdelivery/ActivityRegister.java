package com.techbow.liulunchdelivery;

import java.util.List;
import java.util.Random;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.techbow.liulunchdelivery.Utils.SmsSendRecv;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityRegister extends ActionBarActivity {
	private ActionBar actionBar;
	private EditText editAccount;
	public EditText editCode;
	private Button buttonCode;
	private EditText editPassword;
	private EditText editPasswordAgain;
	private Button register;
	private String randomCode;
	private SmsSendRecv sms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        View customerView = getLayoutInflater().inflate(R.layout.actionbar_logo, null);
		actionBar.setCustomView(customerView);
		actionBar.setDisplayShowCustomEnabled(true);
	    
	    editAccount = (EditText) findViewById(R.id.editAccount);
	    editCode = (EditText) findViewById(R.id.editCode);
	    buttonCode = (Button) findViewById(R.id.buttonCode);
	    editPassword = (EditText) findViewById(R.id.editPassword);
	    editPasswordAgain = (EditText) findViewById(R.id.editPasswordAgain);
	    register = (Button) findViewById(R.id.register);
	    
	    Random random = new Random();
        randomCode = String.valueOf(Math.abs(random.nextInt()%10000));
        sms = new SmsSendRecv(this);
	    
	    buttonCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editAccount.getText() == null) {
					Toast.makeText(ActivityRegister.this, "Please input your phone number...",  Toast.LENGTH_SHORT).show();
					return;
				}
				ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
				userQuery.whereEqualTo("username", editAccount.getText().toString());
				userQuery.findInBackground(new FindCallback<ParseUser>() {
					@Override
					public void done(List<ParseUser> list, ParseException e) {
						// TODO Auto-generated method stub
						if (e == null && list.size() == 0) {
							sms.sendMessage("ID CODE:" + randomCode, editAccount.getText().toString());
						} else if (e == null) {
							Toast.makeText(ActivityRegister.this, "Your phone number has already signed up...",  Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ActivityRegister.this, "Server is not responding, or network is not working. Please try again later...",  Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	    register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editAccount.getText() == null ||
						editCode.getText() == null ||
						editPassword.getText() == null ||
						editPasswordAgain.getText() == null) {
					Toast.makeText(ActivityRegister.this, "Please input all information needed...",  Toast.LENGTH_SHORT).show();
					return;
				}
				if (editCode.getText().toString().equals(randomCode)) {
					if (editPassword.getText().toString().equals(editPasswordAgain.getText().toString())) {
	        			ParseUser user = new ParseUser();
	        			user.setUsername(editAccount.getText().toString());
	        			user.setPassword(editPassword.getText().toString());
	        			user.signUpInBackground(new com.parse.SignUpCallback() {
							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub
								if (e == null) {
	        			            Toast.makeText(ActivityRegister.this, "Congratulations,  sign up is done!", Toast.LENGTH_LONG).show();
	        			            ActivityRegister.this.finish();
	        			        } else {
	        			        	Toast.makeText(ActivityRegister.this, "Fail to sign up. Server is not responding, or network is not working. Please try again later...", Toast.LENGTH_LONG).show();
	        			        }
							}
						});
	        		}
	        		else {
	        			Toast.makeText(ActivityRegister.this, "Passwords not consistent, please input again...",  Toast.LENGTH_SHORT).show();
	        		}
				} else {
					Toast.makeText(ActivityRegister.this, "Wrong ID code, please try again...",  Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sms.deregisterAllReceiver();
	}
}
