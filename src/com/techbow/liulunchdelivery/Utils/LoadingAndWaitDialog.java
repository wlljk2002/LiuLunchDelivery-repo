package com.techbow.liulunchdelivery.Utils;

import com.techbow.liulunchdelivery.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author leonwood
 * this object need to be manipulated in main thread
 */
public class LoadingAndWaitDialog {
	private AlertDialog.Builder loadingAndWaitDialogBuilder;
	private AlertDialog loadingAndWaitDialog = null;
	private TextView status;
	
	public LoadingAndWaitDialog(Activity context) {
		super();
		// TODO Auto-generated constructor stub
		View loadingAndWaitDialogLayout = context.getLayoutInflater().inflate(R.layout.loading_wait_dialog, null);
		status = (TextView) loadingAndWaitDialogLayout.findViewById(R.id.status);
		loadingAndWaitDialogBuilder = new AlertDialog.Builder(context);
		loadingAndWaitDialogBuilder.setView(loadingAndWaitDialogLayout);
	}
	
	public void show() {
		loadingAndWaitDialog = loadingAndWaitDialogBuilder.show();
	}
	
	public void hide() {
		if (loadingAndWaitDialog != null) {
			loadingAndWaitDialog.dismiss();
		}
	}
	
	public void changeStatusWord(String word) {
		status.setText(word);
	}
}
