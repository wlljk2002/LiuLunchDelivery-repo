package com.techbow.liulunchdelivery;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;

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

public class ActivityLogin extends ActionBarActivity {
	private ActionBar actionBar;
	private EditText editAccount;
	private EditText editPassword;
	private Button login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        View customerView = getLayoutInflater().inflate(R.layout.actionbar_logo, null);
	    actionBar.setCustomView(customerView);
	    actionBar.setDisplayShowCustomEnabled(true);
	    
	    editAccount = (EditText) findViewById(R.id.editAccount);
	    editPassword = (EditText) findViewById(R.id.editPassword);
	    login = (Button) findViewById(R.id.login);
	    login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editAccount.getText() == null ||
						editPassword.getText() == null) {
					Toast.makeText(ActivityLogin.this, "Please input all information needed...",  Toast.LENGTH_SHORT).show();
					return;
				}
				AVUser.logInInBackground(editAccount.getText().toString(), editPassword.getText().toString(), new LogInCallback<AVUser>() {
				    public void done(AVUser user, AVException e) {
				        if (user != null) {
				            // µÇÂ¼³É¹¦
				        	Toast.makeText(ActivityLogin.this, "Congratulations,  login is done!", Toast.LENGTH_LONG).show();
				        	finish();
				        } else if (e != null) {
				        	Toast.makeText(ActivityLogin.this, "ActivityLogin. Server is not responding, or network is not working. Please try again later...", Toast.LENGTH_LONG).show();
				        }
				        else {
				            // µÇÂ¼Ê§°Ü
				        	Toast.makeText(ActivityLogin.this, "Login fail, please input again and try...", Toast.LENGTH_LONG).show();
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
