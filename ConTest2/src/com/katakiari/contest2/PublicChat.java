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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class PublicChat extends ActionBarActivity {

	private LocalBroadcastManager mLocalBroadcastManager;
	ListView listview;
	ArrayList<ChatM> listM = new ArrayList<ChatM>();
	ArrayList<String> old_nicks = new ArrayList<String>();
	ArrayList<String> old_messages = new ArrayList<String>();
	MyChatsAdapter adapterC;
	String room_jid;
	String priv;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_chat);
		
		Intent intent = getIntent();
		room_jid = intent.getStringExtra("room_jid");
		priv = intent.getStringExtra("priv");
		old_nicks = intent.getStringArrayListExtra("his_nick");
		old_messages = intent.getStringArrayListExtra("his_mes");
		for (int i = 0; i < old_messages.size(); i++) {
	    	 listM.add(new ChatM(old_nicks.get(i), old_messages.get(i)));
	     }
		
		
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mLocalBroadcastManager.registerReceiver(mMessageReceiver1,
			      new IntentFilter("recv_msg_room"));
		
		listview = (ListView)findViewById(R.id.chat_view);
		
		adapterC = new MyChatsAdapter(PublicChat.this, R.layout.chat_m_row, listM);
        adapterC.setNotifyOnChange(true);
        listview.setAdapter(adapterC);
	}

	
	private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
			 String sender=intent.getStringExtra("sender");
			 String msg=intent.getStringExtra("msg");
			 Log.d("msg from ", sender);
			 adapterC.add(new ChatM(sender, msg));
			 
			 
		  }
		};
	
		public void SendMsg(View view)
		{
			//new ConnectToXmpp().execute();
			EditText et1 = (EditText) findViewById(R.id.editText1);
			String m =  et1.getText().toString();
			adapterC.add( new ChatM("You", m));
			et1.setText("");
			Intent int_send=new Intent("send_msg_room");
			int_send.putExtra("message", m);
			int_send.putExtra("to", room_jid);
			mLocalBroadcastManager.sendBroadcast(int_send);
			
		}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.public_chat, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
	    if (Objects.equals("yes", priv))
	    	return true;
	    else 
	    	return false;
	    
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.delete) {
			Intent i=new Intent("del_room");
			mLocalBroadcastManager.sendBroadcast(i);
		}
		return super.onOptionsItemSelected(item);
	}
}
