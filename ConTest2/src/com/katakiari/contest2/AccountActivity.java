package com.katakiari.contest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AccountActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		// Locate the viewpager in activity_main.xml
				ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		 
				// Set the ViewPagerAdapter into ViewPager
				viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
				viewPager.setOffscreenPageLimit(3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(AccountActivity.this);
	    	dlgAlert.setMessage("Are you sure you want to log out?");
	    	dlgAlert.setTitle("Log Out");
	    	dlgAlert.setPositiveButton("Ok",
	    		    new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	              //dismiss the dialog 
	            	stopService(new Intent(AccountActivity.this, CTService.class));
	            	startActivity(new Intent(AccountActivity.this, MainActivity.class));
	            }
	        });
	    	dlgAlert.setCancelable(true);
	    	dlgAlert.create().show();
	    	
	    
		}
		return super.onOptionsItemSelected(item);
	}
}
