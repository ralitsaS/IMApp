package com.katakiari.contest2;

import java.util.ArrayList;
import java.util.Objects;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class PrivateChat extends Activity {

	private LocalBroadcastManager mLocalBroadcastManager;
	ListView listview;
	ArrayAdapter<String> adapter;
	String user_jid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_chat);
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mLocalBroadcastManager.registerReceiver(mMessageReceiver1,
			      new IntentFilter("recv_msg"));
		
		listview = (ListView)findViewById(R.id.chat_view);

		adapter =
	            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        

        adapter.setNotifyOnChange(true);
        listview.setAdapter(adapter);
		
		Intent intent = getIntent();
		String user_all = intent.getStringExtra("user");
	    user_jid= user_all.split("\\(")[0];
	    
	}

	private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
			 String sender=intent.getStringExtra("sender");
			 String msg=intent.getStringExtra("message");
			 Log.d("msg from ", sender);
			 if(Objects.equals(sender, user_jid)){
			 adapter.add(msg);
			 
			 View wantedView = adapter.getView(adapter.getPosition(msg), null, listview);
			 //wantedView.getBackground().setColorFilter(Color.parseColor("#00ff00"), null);
			 wantedView.setBackgroundColor(Color.parseColor("#00ff00"));
			 }
		  }
		};
		
		public void SendMsg(View view)
		{
			//new ConnectToXmpp().execute();
			EditText et1 = (EditText) findViewById(R.id.editText1);
			String m =  et1.getText().toString();
			adapter.add(m);
			et1.setText("");
			Intent int_send=new Intent("send_msg");
			int_send.putExtra("message", m);
			int_send.putExtra("to", user_jid);
			mLocalBroadcastManager.sendBroadcast(int_send);
			
		}
		
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_chat, menu);
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
}
