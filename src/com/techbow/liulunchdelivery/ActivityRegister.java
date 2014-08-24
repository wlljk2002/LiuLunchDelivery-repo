package com.techbow.liulunchdelivery;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;

import android.content.res.Configuration;
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
	private EditText editCode;
	private Button buttonCode;
	private EditText editPassword;
	private EditText editPasswordAgain;
	private Button register;
	
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
	    
	    buttonCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editAccount.getText() == null) {
					Toast.makeText(ActivityRegister.this, "Please input your phone number...",  Toast.LENGTH_SHORT).show();
					return;
				}
//				try {
//					AVOSCloud.requestSMSCode(editAccount.getText().toString(), "老刘食堂", "注册", 10);
//					Toast.makeText(ActivityRegister.this, "ID code has been requested, please check coming message...",  Toast.LENGTH_LONG).show();
//				} catch (AVException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Toast.makeText(ActivityRegister.this, "Fail to request ID code. Server is not responding, or network is not working. Please try again later...",  Toast.LENGTH_LONG).show();
//				}
				AVOSCloud.requestSMSCodeInBackgroud(editAccount.getText().toString(), new RequestMobileCodeCallback() {
					@Override
					public void done(AVException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							Toast.makeText(ActivityRegister.this, "ID code has been requested, please check coming message...",  Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(ActivityRegister.this, "Fail to request ID code. Server is not responding, or network is not working. Please try again later...",  Toast.LENGTH_LONG).show();
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
			    AVOSCloud.verifySMSCodeInBackground(editCode.getText().toString(), new AVMobilePhoneVerifyCallback() {
			        @Override
			        public void done(AVException e) {
			        //此处可以完成用户想要完成的操作
			        	if (e == null) {
			        		if (editPassword.getText().toString().equals(editPasswordAgain.getText().toString())) {
			        			AVUser user = new AVUser();
			        			user.setUsername(editAccount.getText().toString());
			        			user.setPassword(editPassword.getText().toString());
			        			user.signUpInBackground(new SignUpCallback() {
			        			    public void done(AVException e) {
			        			        if (e == null) {
			        			            Toast.makeText(ActivityRegister.this, "Congratulations,  sign up is done!", Toast.LENGTH_LONG).show();
			        			        } else {
			        			        	Toast.makeText(ActivityRegister.this, "Fail to sign up. Server is not responding, or network is not working. Please try again later...", Toast.LENGTH_LONG).show();
			        			        }
			        			    }
			        			});
			        		}
			        		else {
			        			Toast.makeText(ActivityRegister.this, "Passwords not consistent, please input again...",  Toast.LENGTH_SHORT).show();
			        		}
			        	}
			        	else {
			        		Toast.makeText(ActivityRegister.this, "Wrong ID code, please try again...",  Toast.LENGTH_SHORT).show();
			        	}
			        }
			      });
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
}
